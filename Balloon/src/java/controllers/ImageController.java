package controllers;

import com.sun.xml.rpc.processor.modeler.j2ee.xml.string;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import static com.sun.faces.el.FacesCompositeELResolver.ELResolverChainType.Faces;
import entities.Image;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import entities.Album;
import entities.Love;
import facades.ImageFacade;
import java.io.File;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map;import java.net.URL;import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@ManagedBean(name = "imageController")
@SessionScoped
public class ImageController implements Serializable {
    
       
    private Image current;
    private DataModel items = null;
    @EJB
    private facades.ImageFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    
    @ManagedProperty(value="#{albumController}")
    private AlbumController albumController;
    
    private Part file1;

    public Part getFile1() {
        return file1;
    }

    public void setFile1(Part file1) {
        this.file1 = file1;
    }

    public ImageController() {
    }
    
    //public uploadFile(){
    //}
    
    public void editImageFromAlbum(){
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/Edit.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idImg = Integer.parseInt(params.get("idImage"));
        current = getFacade().getImage(idImg);
    }
    
    public void showImageFromAlbum()
    {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/View.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idImg = Integer.parseInt(params.get("idImage"));
        current = getFacade().getImage(idImg);
    }
    public String addImageFromAlbum(){
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/Create.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int fkAlbum = Integer.parseInt(params.get("idAlbum"));
        
        current = new Image();
        selectedItemIndex = -1;
        current.setFkAlbum(getFacade().getAlbum(fkAlbum));
        
        return "Create";
    }

    public Image getSelected() {
        if (current == null) {
            current = new Image();
            selectedItemIndex = -1;
        }
        return current;
    }
    private ImageFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Image) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Image();
        selectedItemIndex = -1;
        return "Create";
    }

    private static String getFilename(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
                return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
            }
        }
        return null;
    }
    
    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<FacesMessage>();
        Part file = (Part)value;
        //TODO ?
        /**if (file.getSize() > 1024) {
          msgs.add(new FacesMessage("Image est trop grande !"));
        }**/
        if (!("image/jpeg".equals(file.getContentType()) || "image/png".equals(file.getContentType()))) {
          msgs.add(new FacesMessage("Une image JPG ou PNG" ));
        }
        if (!msgs.isEmpty()) {
          throw new ValidatorException(msgs);
        }
      }
   
    
    public String create() {
        try {
            //upload file
            InputStream input = file1.getInputStream();
            
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            Files.copy(input, new File(context.getInitParameter("uploadDirectory"), current.getIName()+"_"+getFilename(file1)).toPath());
            
            //set fileName
            //KEEP IT
            current.setIFilename(current.getIName()+"_"+getFilename(file1));
            
            // HELP
            readMetaData(current.getIFilename(), current, context);
            
            //Save
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ImageCreated"));
            
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/album/View.xhtml");
            albumController.setCurrent(current.getFkAlbum());
            return "View";
            
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Image) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ImageUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Image) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
          
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String destroy(Image image){
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        Album album = current.getFkAlbum();
        performDestroy();
        recreateModel();
        updateCurrentItem();
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/album/View.xhtml");
        albumController.setCurrent(album);
        
        return "View";
    }

    private void performDestroy() {
        
        try{
            FacesContext fc = FacesContext.getCurrentInstance();
            ServletContext context = (ServletContext) fc.getExternalContext().getContext();
            File file = new File(context.getInitParameter("uploadDirectory")+current.getIFilename());

            if(!file.delete())
            {
                System.out.println("COULD NOT DELETE THE FILE"+file.getPath());
            }

            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ImageDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Image.class)
    public static class ImageControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ImageController controller = (ImageController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "imageController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Image) {
                Image o = (Image) object;
                return getStringKey(o.getIdImage());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Image.class.getName());
            }
        }
    }
    
    public void readMetaData(String imageName, Image image, ServletContext context)
    {   
        try {
            
            File file = new File(context.getInitParameter("uploadDirectory")+imageName);
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            /*for (Directory directory : metadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }
            }*/
            
            ExifSubIFDDirectory directorySub = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            ExifIFD0Directory directory0 = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            
            System.out.println("CAMERA IMAGE TAG "+directory0.getString(ExifIFD0Directory.TAG_MODEL));
            System.out.println("COPYRIGHT IMAGE TAG "+directorySub.getString(ExifSubIFDDirectory.TAG_COPYRIGHT));
           
            Date date = directorySub.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
            if(date != null)
            {
                image.setIdateCapture(date);
            }
            
            String camera = directory0.getString(ExifIFD0Directory.TAG_MODEL);
            if(camera != null)
            {
                image.setICamera(camera);
            }
            
            String copyright = directorySub.getString(ExifSubIFDDirectory.TAG_COPYRIGHT);
            if(copyright != null)
            {
                image.setICopyright(copyright);
            }
        }
        catch (Exception e) {
            System.out.println("READ IMAGE EXCEPTION !!!");
            e.printStackTrace();
        }

    }
    
    public void love(String username)
    {
        Love love = new Love();
        love.setFkImage(current);
        current.addLove(love);
        love.setFkUser(getFacade().getCurrentUser(username));
        getFacade().love(love);
    }
    
    public void unlove(String username)
    {
        getFacade().unlove(current, username);
    }
    
    public boolean canLove(String username, Image image)
    {
        return getFacade().canLove(username, image);
    }
    
    public boolean isImageOwner(String username)
    {
        return getFacade().isImageOwner(username, current);
    }
    
    public AlbumController getAlbumController()
    {
    return albumController;
    }

    public void setAlbumController(AlbumController albumController)
    {
    this.albumController = albumController;
    }
    
    public String returnToAlbum()
    {
        ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)context.getRequest();
        HttpServletResponse response = (HttpServletResponse)context.getResponse();

        String referer = request.getHeader("Referer");
        try {
            response.sendRedirect(referer);
        } catch (IOException ex) {
            Logger.getLogger(ImageController.class.getName()).log(Level.SEVERE, null, ex);
            return "Index";
        }
        
        return null;
    }
}
