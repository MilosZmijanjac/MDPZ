package soap;

public class UserServiceProxy implements soap.UserService {
  private String _endpoint = null;
  private soap.UserService userService = null;
  
  public UserServiceProxy() {
    _initUserServiceProxy();
  }
  
  public UserServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initUserServiceProxy();
  }
  
  private void _initUserServiceProxy() {
    try {
      userService = (new soap.UserServiceServiceLocator()).getUserService();
      if (userService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)userService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)userService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (userService != null)
      ((javax.xml.rpc.Stub)userService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public soap.UserService getUserService() {
    if (userService == null)
      _initUserServiceProxy();
    return userService;
  }
  
  public model.User[] getUsers() throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.getUsers();
  }
  
  public model.User login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.login(username, password);
  }
  
  public void logout(model.User user) throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    userService.logout(user);
  }
  
  public model.User addNewUser(java.lang.String name, java.lang.String password, java.lang.String station_id, java.lang.String xml_file) throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.addNewUser(name, password, station_id, xml_file);
  }
  
  public boolean removeUser(model.User user) throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.removeUser(user);
  }
  
  public java.lang.String[] getStationIds() throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.getStationIds();
  }
  
  public model.User[] getUsersByStationId(java.lang.String station_id) throws java.rmi.RemoteException{
    if (userService == null)
      _initUserServiceProxy();
    return userService.getUsersByStationId(station_id);
  }
  
  
}