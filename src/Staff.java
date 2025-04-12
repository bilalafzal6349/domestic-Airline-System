class Staff extends User {
    private String designation;

    public Staff(String id, String name, String password, String designation) {
        super(id, name, password);
        this.designation = designation;
    }

    @Override
    public void displayInfo() {
        System.out.println("Staff ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Designation: " + designation);
    }

    public void staffMenu() {
        System.out.println("Staff Menu:");
        System.out.println("1. Add a Flight");
        System.out.println("2. Remove a Flight");
        System.out.println("3. View Passengers");
        System.out.println("4. Logout");
    }
}
