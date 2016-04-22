package controllers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import entities.Image;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import entities.Love;
import facades.ImageFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.util.Map;import java.net.URL;import java.util.Date;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "imageController")
@SessionScoped
public class ImageController implements Serializable {
       
    private Image current;
    private DataModel items = null;
    @EJB
    private facades.ImageFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    public ImageController() {
    }
    
    public void editImageFromAlbum(){
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/Edit.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idImg = Integer.parseInt(params.get("idImage"));
        current = getFacade().getImage(idImg);
    }
    
    public void addImageFromAlbum(){
        
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/Create.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int fkAlbum = Integer.parseInt(params.get("idAlbum"));
        
        current = new Image();
        selectedItemIndex = -1;
        current.setFkAlbum(getFacade().getAlbum(fkAlbum));
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

    public String create() {
        try {

            readMetaData(current.getIFilename(), current.getIName().replaceAll(" ", ""), current);
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ImageCreated"));
            return prepareCreate();
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
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
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
    
    public void readMetaData(String urlString, String pictureName, Image image)
    {  
        System.out.println("READ IMAGE START !!! 2");
        try {
                
            String pictureAddress = "OK";
            pictureAddress += pictureName;
            pictureAddress += ".jpg";    
            
            System.out.println("MyAddress : "+pictureAddress);

            String pictureLongName = pictureName;
            pictureLongName += ".jpg";
            image.setIFilename(pictureLongName);
            
            URL url = new URL(urlString);
            OutputStream os;
            try (InputStream is = url.openStream()) {
                os = new FileOutputStream(pictureAddress);
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
            }
            os.close();

            File file = new File(pictureAddress);
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
        System.out.println("READ IMAGE END !!!");
    }
    
    public void love(String username)
    {
        Love love = new Love();
        love.setFkImage(current);
        current.addLove(love);
        getFacade().love(love, username);
    }
    
    public boolean canLove(String username, Image image)
    {
        return getFacade().canLove(username, image);
    }

}
