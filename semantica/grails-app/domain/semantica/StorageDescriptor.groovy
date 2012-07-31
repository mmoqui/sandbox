package semantica

class StorageDescriptor {

  static constraints = {
    name unique: true
    location blank: false
  }
  
  String name
  String location
}
