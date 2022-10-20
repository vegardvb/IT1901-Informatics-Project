package bibtek.core;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Container for all User instances in storage.
 *
 */
public final class UserMap implements Iterable<User> {

    private final Map<String, User> users = new LinkedHashMap<>();

    /**
     * Removes the User from this UserMap.
     *
     * @param username of User to be removed
     * @throws IllegalArgumentException if the user's name is invalid
     */
    public void removeUser(final String username) {
        users.remove(username);
    }

    /**
     * Gets the User with the provided username.
     *
     * @param username the username
     * @return the User with the provided username
     */
    public User getUser(final String username) {
        return users.get(username);
    }

    /**
     * Replaces an existing User with the same username, or adds it.
     *
     * @param user the User
     */
    public void putUser(final User user) {
        users.put(user.getUserName(), user);
    }

    @Override
    public Iterator<User> iterator() {
        return users.values().iterator();
    }

    /**
     * Equals method of UserMap.
     */
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        for (User user : this) {
            if (!((UserMap) obj).users.containsKey(user.getUserName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return users.toString().hashCode();
    }

    @Override
    public String toString() {
        return this.users.toString();
    }

}
