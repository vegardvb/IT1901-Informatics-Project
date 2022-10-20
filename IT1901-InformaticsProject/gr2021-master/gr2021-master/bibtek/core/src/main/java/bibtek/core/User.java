package bibtek.core;

public class User {

    /**
     * The minimal age the user can be.
     */
    public static final int MINIMAL_AGE = 13;
    /**
     * User attributes.
     */
    private String userName;

    private int age;

    private Library library;

    /**
     * Creates a file.
     *
     * @param userName
     * @param age
     * @param library
     */
    public User(final String userName, final int age, final Library library) {
        this.userName = userName;
        this.age = age;
        this.library = library;

    }

    /**
     * Constructor that creates a User with a userName and age and an empty library.
     *
     * @param userName
     * @param age
     */
    public User(final String userName, final int age) {
        this(userName, age, new Library());

    }

    /**
     * @return the username of this user.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     *
     * @return user's age
     */
    public int getAge() {
        return this.age;
    }

    /**
     *
     * @return user's library
     */
    public Library getLibrary() {
        return this.library;
    }

    /**
     * Sets user's username.
     *
     * @param name
     */
    public void setUserName(final String name) {
        this.userName = name;
    }

    /**
     * Change the age of the user.
     *
     * @param userAge
     */
    public void setAge(final int userAge) {
        this.age = userAge;
    }

    /**
     * Change the library of the user.
     *
     * @param lib
     */
    public void setLibrary(final Library lib) {
        this.library = lib;
    }

    /**
     * toString method for this class.
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("user: { \n");
        sb.append("userName: " + getUserName() + "\n");
        sb.append("age: " + getAge() + "\n");
        sb.append("library: " + getLibrary().toString() + "\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Equals method for User.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        User u = (User) obj;
        return this.userName.equals(u.getUserName()) && this.age == u.getAge() && this.library.equals(u.getLibrary());
    }

    /**
     * hashCode method.
     */
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

}
