/**
 * UserService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package soap;

public interface UserService extends java.rmi.Remote {
    public model.User[] getUsers() throws java.rmi.RemoteException;
    public model.User login(java.lang.String username, java.lang.String password) throws java.rmi.RemoteException;
    public void logout(model.User user) throws java.rmi.RemoteException;
    public model.User addNewUser(java.lang.String name, java.lang.String password, java.lang.String station_id, java.lang.String xml_file) throws java.rmi.RemoteException;
    public boolean removeUser(model.User user) throws java.rmi.RemoteException;
    public java.lang.String[] getStationIds() throws java.rmi.RemoteException;
    public model.User[] getUsersByStationId(java.lang.String station_id) throws java.rmi.RemoteException;
}
