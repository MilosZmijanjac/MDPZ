/**
 * User.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package model;

public class User  implements java.io.Serializable {
    private java.lang.String _password;

    private java.lang.Integer _port;

    private java.lang.String _salt;

    private java.lang.String _stationID;

    private java.lang.String _username;

    public User() {
    }

    public User(
           java.lang.String _password,
           java.lang.Integer _port,
           java.lang.String _salt,
           java.lang.String _stationID,
           java.lang.String _username) {
           this._password = _password;
           this._port = _port;
           this._salt = _salt;
           this._stationID = _stationID;
           this._username = _username;
    }


    /**
     * Gets the _password value for this User.
     * 
     * @return _password
     */
    public java.lang.String get_password() {
        return _password;
    }


    /**
     * Sets the _password value for this User.
     * 
     * @param _password
     */
    public void set_password(java.lang.String _password) {
        this._password = _password;
    }


    /**
     * Gets the _port value for this User.
     * 
     * @return _port
     */
    public java.lang.Integer get_port() {
        return _port;
    }


    /**
     * Sets the _port value for this User.
     * 
     * @param _port
     */
    public void set_port(java.lang.Integer _port) {
        this._port = _port;
    }


    /**
     * Gets the _salt value for this User.
     * 
     * @return _salt
     */
    public java.lang.String get_salt() {
        return _salt;
    }


    /**
     * Sets the _salt value for this User.
     * 
     * @param _salt
     */
    public void set_salt(java.lang.String _salt) {
        this._salt = _salt;
    }


    /**
     * Gets the _stationID value for this User.
     * 
     * @return _stationID
     */
    public java.lang.String get_stationID() {
        return _stationID;
    }


    /**
     * Sets the _stationID value for this User.
     * 
     * @param _stationID
     */
    public void set_stationID(java.lang.String _stationID) {
        this._stationID = _stationID;
    }


    /**
     * Gets the _username value for this User.
     * 
     * @return _username
     */
    public java.lang.String get_username() {
        return _username;
    }


    /**
     * Sets the _username value for this User.
     * 
     * @param _username
     */
    public void set_username(java.lang.String _username) {
        this._username = _username;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._password==null && other.get_password()==null) || 
             (this._password!=null &&
              this._password.equals(other.get_password()))) &&
            ((this._port==null && other.get_port()==null) || 
             (this._port!=null &&
              this._port.equals(other.get_port()))) &&
            ((this._salt==null && other.get_salt()==null) || 
             (this._salt!=null &&
              this._salt.equals(other.get_salt()))) &&
            ((this._stationID==null && other.get_stationID()==null) || 
             (this._stationID!=null &&
              this._stationID.equals(other.get_stationID()))) &&
            ((this._username==null && other.get_username()==null) || 
             (this._username!=null &&
              this._username.equals(other.get_username())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (get_password() != null) {
            _hashCode += get_password().hashCode();
        }
        if (get_port() != null) {
            _hashCode += get_port().hashCode();
        }
        if (get_salt() != null) {
            _hashCode += get_salt().hashCode();
        }
        if (get_stationID() != null) {
            _hashCode += get_stationID().hashCode();
        }
        if (get_username() != null) {
            _hashCode += get_username().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(User.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://model", "User"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_password");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model", "_password"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_port");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model", "_port"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_salt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model", "_salt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_stationID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model", "_stationID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_username");
        elemField.setXmlName(new javax.xml.namespace.QName("http://model", "_username"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
