package fr.triedge.todo.database;

import fr.triedge.todo.model.*;
import fr.triedge.todo.utils.PWDManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class DB {

    private static DB instance;
    private Connection connection;

    private DB(){}

    public static DB getInstance(){
        if (instance == null){
            instance = new DB();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            resetConnection();
        }
        return connection;
    }

    public void resetConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        PWDManager manager = new PWDManager();
        String pwd = manager.decode("JGJpdXNlclMjODg=");
        String host = "localhost";
        if (System.getProperty("host") != null){
            host = System.getProperty("host");
        }
        connection = DriverManager.getConnection("jdbc:mysql://"+host+"/amadeus","amadeus",pwd);
    }

    public ArrayList<Project> getProjects() throws SQLException {
        ArrayList<Project> projects = new ArrayList<>();
        String sql = "select * from td_project order by project_name";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            Project p = new Project(res);
            projects.add(p);
        }
        res.close();
        stmt.close();
        return projects;
    }

    public ArrayList<Entry> getEntriesFilter(boolean isArchived) throws SQLException {
        ArrayList<Entry> entries = new ArrayList<>();
        String filter = " ";
        if (isArchived)
            filter = " where status_name = 'Closed' ";
        String sql = "select * from td_entry left join ama_user on entry_user=user_id left join td_status on status_id=entry_status left join td_project on entry_project=project_id" + filter + "order by project_priority, entry_priority, entry_name asc";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while(res.next()){
            Project p = new Project(res);
            Status s = new Status(res);
            User u = new User(res);
            Entry e = new Entry(res);
            e.setProject(p);
            e.setStatus(s);
            e.setUser(u);
            entries.add(e);
        }
        res.close();
        stmt.close();
        return entries;
    }

    public void updateEntry(Entry entry, int id) throws SQLException {
        if (entry == null)
            return;
        String sql = "update td_entry set entry_name=?, entry_desc=?, entry_priority=?, entry_project=? where entry_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, entry.getName());
        stmt.setString((int)2, entry.getDescription());
        stmt.setInt((int)3, entry.getPriority());
        stmt.setInt((int)4, entry.getProject().getId());
        stmt.setInt((int)5, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public ArrayList<Person> getPersonsByLastVisited() throws SQLException {
        ArrayList<Person> persons = new ArrayList<>();
        String sql = "select * from td_person order by person_last_visited asc";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while(res.next()){
            Person p = new Person(res);
            persons.add(p);
        }
        res.close();
        stmt.close();
        return persons;
    }

    public Person getPerson(int id) throws SQLException {
        Person p = null;
        String sql = "select * from td_person where person_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        ResultSet res = stmt.executeQuery();
        while(res.next()){
            p = new Person(res);
        }
        res.close();
        stmt.close();
        return p;
    }

    public void updatePerson(Person person, int id) throws SQLException {
        if (person == null)
            return;
        String sql = "update td_person set " +
                "person_username=?, " +
                "person_firstname=?, " +
                "person_lastname=?, " +
                "person_location=?, " +
                "person_is_admin=?, " +
                "person_mol_edit=?, " +
                "person_info=?, " +
                "person_lang=?, " +
                "person_last_visited=? " +
                "where person_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        int idx = 0;
        stmt.setString(++idx, person.getUsername());
        stmt.setString(++idx, person.getFirstname());
        stmt.setString(++idx, person.getLastname());
        stmt.setString(++idx, person.getLocation());
        stmt.setBoolean(++idx, person.isAdmin());
        stmt.setBoolean(++idx, person.isCanEditMol());
        stmt.setString(++idx, person.getInfo());
        stmt.setString(++idx, person.getLang());
        if (person.getLastVisited() != null){
            Timestamp tt = new Timestamp(person.getLastVisited().getTime());
            stmt.setTimestamp(++idx, tt);
        }else{
            stmt.setTimestamp(++idx, null);
        }
        stmt.setInt(++idx, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void deletePerson(int id) throws SQLException {
        String sql = "delete from td_person where person_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int) 1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createNewPerson(String username, String firstname, String lastname, String location, boolean isAdmin, boolean canEditMol, String info, String lang) throws SQLException {
        String sql = "insert into td_person (person_username, person_firstname, person_lastname, person_location, person_is_admin, person_mol_edit, person_info, person_lang)values(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        int idx = 0;
        stmt.setString(++idx, username);
        stmt.setString(++idx, firstname);
        stmt.setString(++idx, lastname);
        stmt.setString(++idx, location);
        stmt.setBoolean(++idx, isAdmin);
        stmt.setBoolean(++idx, canEditMol);
        stmt.setString(++idx, info);
        stmt.setString(++idx, lang);
        stmt.executeUpdate();
        stmt.close();
    }

    public void createNewProject(String name, int priority) throws SQLException {
        String sql = "insert into td_project(project_name,project_priority)values(?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, name);
        stmt.setInt((int)2, priority);
        stmt.executeUpdate();
        stmt.close();
    }

    public ArrayList<Entry> getEntries() throws SQLException {
        return getEntriesFilter(false);
    }

    public ArrayList<Entry> getArchivedEntries() throws SQLException {
        return getEntriesFilter(true);
    }

    public void createNewEntry(String name, int projectId, int userId) throws SQLException {
        String sql = "insert into td_entry(entry_name,entry_project,entry_user)values(?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, name);
        stmt.setInt((int)2, projectId);
        stmt.setInt((int)3, userId);
        stmt.executeUpdate();
        stmt.close();
    }

    public Entry getEntry(int id) throws SQLException {
        Entry entry = null;
        String sql = "select * from td_entry left join ama_user on entry_user=user_id left join td_status on status_id=entry_status left join td_project on entry_project=project_id where entry_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        ResultSet res = stmt.executeQuery();
        while(res.next()){
            Project p = new Project(res);
            Status s = new Status(res);
            User u = new User(res);
            entry = new Entry(res);
            entry.setProject(p);
            entry.setStatus(s);
            entry.setUser(u);
        }
        res.close();
        stmt.close();
        return entry;
    }

    public Project getProject(int id) throws SQLException {
        Project p = null;
        String sql = "select * from td_project where project_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            p = new Project(res);
        }
        res.close();
        stmt.close();
        return p;
    }

    public void deleteProject(int id) throws SQLException {
        String sql = "delete from td_project where project_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void deleteEntry(int id) throws SQLException {
        String sql = "delete from td_entry where entry_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void changeStatus(int entryId, int statusId) throws SQLException {
        String sql = "update td_entry set entry_status=? where entry_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, statusId);
        stmt.setInt((int)2, entryId);
        stmt.executeUpdate();
        stmt.close();
    }

    public User loginUser(String name, String password, boolean isEncrypted) throws SQLException {
        if (name == null || password == null)
            return null;
        String pwd = password;
        if (isEncrypted)
            pwd = new PWDManager().encode(password);
        String sql = "select * from ama_user where user_name=? and user_password2=?";
        User user = null;
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, name);
        stmt.setString((int)2, pwd);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            user = new User();
            user.setUserId(res.getInt("user_id"));
            user.setName(res.getString("user_name"));
            user.setLevel(res.getInt("user_level"));
            user.setDisplayName(res.getString("user_display_name"));
        }
        res.close();
        stmt.close();

        return user;
    }

    public User loadUser(String username) throws SQLException {
        String sql = "select * from ama_user where user_name=?";
        User user = null;
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, username);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            user = new User();
            user.setUserId(res.getInt("user_id"));
            user.setName(res.getString("user_name"));
            user.setLevel(res.getInt("user_level"));
            user.setDisplayName(res.getString("user_display_name"));
        }
        res.close();
        stmt.close();

        return user;
    }

    public ArrayList<Event> getEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "select * from td_event";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while (res.next()){
            events.add(buildEvent(res));
        }
        res.close();
        stmt.close();

        return events;
    }

    public Event getEvent(int id) throws SQLException {
        String sql = " select * from td_event where event_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        ResultSet res = stmt.executeQuery();
        Event e = null;
        if (res.next()){
            e = buildEvent(res);
        }
        res.close();
        stmt.close();
        return e;
    }

    private Event buildEvent(ResultSet res) throws SQLException {
        Event e = new Event();
        e.setId(res.getInt("event_id"));
        e.setTitle(res.getString("event_title"));
        e.setDescription(res.getString("event_description"));
        e.setType(res.getString("event_type"));
        e.setBadge(res.getString("event_badge"));
        e.setColor(res.getString("event_color"));
        e.setNotify(res.getBoolean("event_notify"));
        e.setEveryYear(res.getBoolean("event_every_year"));
        Timestamp t = res.getTimestamp("event_date");
        if (t != null){
            e.setDate(new Date(t.getTime()));
        }
        return e;
    }

    public void insertEvent(Event e) throws SQLException {
        String sql = "insert into td_event(event_title,event_description,event_date,event_type,event_color,event_every_year,event_notify,event_badge)values(?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, e.getTitle());
        stmt.setString((int)2, e.getDescription());
        if (e.getDate() != null)
            stmt.setDate((int)3, new java.sql.Date(e.getDate().getTime()));
        stmt.setString((int)4, e.getType());
        stmt.setString((int)5, e.getColor());
        stmt.setBoolean((int)6, e.isEveryYear());
        stmt.setBoolean((int)7, e.isNotify());
        stmt.setString((int)8, e.getBadge());
        stmt.executeUpdate();
        stmt.close();
    }

    public void updateEvent(Event e, int id) throws SQLException {
        String sql = "update td_event set event_title=?,event_description=?,event_date=?,event_type=?,event_color=?,event_every_year=?,event_notify=?,event_badge=? where event_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setString((int)1, e.getTitle());
        stmt.setString((int)2, e.getDescription());
        if (e.getDate() != null){
            Timestamp t = new Timestamp(e.getDate().getTime());
            stmt.setTimestamp((int)3, t);
        }else{
            stmt.setTimestamp((int)3, null);
        }
        stmt.setString((int)4, e.getType());
        stmt.setString((int)5, e.getColor());
        stmt.setBoolean((int)6, e.isEveryYear());
        stmt.setBoolean((int)7, e.isNotify());
        stmt.setString((int)8, e.getBadge());
        stmt.setInt((int)9, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public void deleteEvent(int id) throws SQLException {
        String sql = "delete from td_event where event_id=?";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        stmt.setInt((int)1, id);
        stmt.executeUpdate();
        stmt.close();
    }

    public ArrayList<Event> getNotifiedEvents() throws SQLException {
        ArrayList<Event> evs = new ArrayList<>();
        String sql = "select * from td_event where event_date >= DATE_SUB(NOW(),INTERVAL 1 DAY) and event_date <= DATE_ADD(NOW(), INTERVAL 1 MONTH) and event_notify=true order by event_date asc";
        PreparedStatement stmt = getConnection().prepareStatement(sql);
        ResultSet res = stmt.executeQuery();
        while(res.next()){
            evs.add(buildEvent(res));
        }
        res.close();
        stmt.close();
        return evs;
    }
}
