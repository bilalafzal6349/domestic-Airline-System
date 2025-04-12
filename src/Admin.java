class Admin extends User {
    public Admin(String id, String name, String password) {
        super(id, name, password);
    }

    @Override
    public void displayInfo() {
        System.out.println("Admin ID: " + id);
        System.out.println("Name: " + name);
    }
}
