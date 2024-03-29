package controllers;

import entities.Album;
import controllers.util.JsfUtil;
import controllers.util.PaginationHelper;
import entities.Image;import entities.Love;
import entities.User;import facades.AlbumFacade;
import java.io.File;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



@ManagedBean(name = "albumController")
@SessionScoped
public class AlbumController implements Serializable {

    public Album current;
    
    private DataModel items = null;
    @EJB
    private facades.AlbumFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    
    private List<Album> listRandAlbum;

    public List<Album> getListRandAlbum() {
        //listRandAlbum=getFacade().getVisibleAlbums(); //todo
        listRandAlbum = getFacade().findAll();
        
        if(listRandAlbum.size() > 3){
            listRandAlbum = listRandAlbum.subList(0, 3);
        }
        
        return listRandAlbum;
    }

    public void setListRandAlbum(List<Album> listRandAlbum) {
        this.listRandAlbum = listRandAlbum;
    }
    
    
    public AlbumController() {

    }

    public void setCurrent(Album album)
    {
        current = album;
    }
            
    public void destroyImage() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idImg = Integer.parseInt(params.get("idImage"));
        String filenameImage = params.get("filenameImage");
        
        System.out.println("destroyImage : Called with parameter "+idImg);
        
        ServletContext context = (ServletContext) fc.getExternalContext().getContext();
        File file = new File(context.getInitParameter("uploadDirectory")+filenameImage);
            
        if(!file.delete())
        {
            System.out.println("COULD NOT DELETE THE FILE"+file.getPath());
        }
        
        getFacade().removeImage(idImg, current);
    }
    
    public void editImage() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/image/Edit.xhtml");
    }
    
    public Album getSelected() {
        if (current == null) {
            current = new Album();
            selectedItemIndex = -1;
        }
        return current;
    }


    private AlbumFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(8) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize() - 1}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    
    public String prepareViewAlbum(){

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/album/View.xhtml");

        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idAlbum = Integer.parseInt(params.get("idAlbum"));
        
        current = getFacade().getAlbum(idAlbum);

        //selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        
        return "View";
    }

    public String prepareView() {
        
        System.out.println("ALBUM prepareView");
        current = (Album) getItems().getRowData();
        //updateCurrentItem();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        current = new Album();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            HttpServletRequest request = (HttpServletRequest)context.getRequest();
            User currentUser = getFacade().getCurrentUser(request.getRemoteUser());
            current.setFkUser(currentUser);
            
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlbumCreated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Album) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlbumUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Album) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String destroyAndViewList() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        recreateModel();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("AlbumDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }
    
    public void returnAlbum() {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/album/View.xhtml");
        
        FacesContext fc = FacesContext.getCurrentInstance();
        Map<String,String> params = fc.getExternalContext().getRequestParameterMap();
	int idAlbum = Integer.parseInt(params.get("idAlbum"));
        current = getFacade().getAlbum(idAlbum);
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
    
    public int getCurrentPage() {
        if(pagination.getItemsCount() == 0)
        {
            return 1;
        }
        return ((pagination.getPageFirstItem() + 1)/ pagination.getItemsCount()) + 1;
    }
        
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = Album.class)
    public static class AlbumControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            AlbumController controller = (AlbumController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "albumController");
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
            if (object instanceof Album) {
                Album o = (Album) object;
                return getStringKey(o.getIdAlbum());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Album.class.getName());
            }
        }

    }
    
    public boolean isAlbumOwner(Album album, String username)
    {
        return getFacade().isAlbumOwner(username, album);
    }
    
    public void love(Image img, String username)
    {
        System.out.println("LOVE FROM ALBUM !!");
        Love love = new Love();
        love.setFkImage(img);
        img.addLove(love);
        love.setFkUser(getFacade().getCurrentUser(username));
        getFacade().love(love);
        System.out.println("END OF LOVE FROM ALBUM !!");
    }
    
    public void unlove(Image img, String username)
    {
        getFacade().unlove(img, username);
    }
    
    public boolean canLove(String username, Image image)
    {
        return getFacade().canLove(username, image);
    }
    
    public String accessList()
    {
        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "/album/List.xhtml");
        return "List";
    }
    
    public String getUploadDirectory()
    {
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        return context.getInitParameter("uploadDirectory");
    }
}
