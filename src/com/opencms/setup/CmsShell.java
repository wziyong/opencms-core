package com.opencms.setup;

import java.util.*;
import java.io.*;
import com.opencms.file.*;
import com.opencms.core.*;
import java.lang.reflect.*;

/**
 * This class is a commadnlineinterface for the opencms. It can be used to test
 * the opencms, and for the initial setup. It uses the OpenCms-Object.
 * 
 * @author Andreas Schouten
 * @version $Revision: 1.8 $ $Date: 2000/01/11 19:07:50 $
 */
public class CmsShell implements I_CmsConstants {
	
	/**
	 * The resource broker to get access to the cms.
	 */
	private I_CmsResourceBroker m_rb;

	/**
	 * The resource broker to get access to the cms.
	 */
	private A_CmsObject m_cms;

	/**
	 * The main entry point for the commandline interface to the opencms. 
	 *
	 * @param args Array of parameters passed to the application
	 * via the command line.
	 */
	public static void main (String[] args)	{
		
		CmsShell shell = new CmsShell();
		
		try {
		
			if( (args.length == 0) || (args.length > 3) ) {
				// print out usage-information.
				shell.usage();
			} else {
		
				// initializes the db and connects to it
				shell.init(args);
				
				// print the version-string
				shell.version();
		
				// wait for user-input
				shell.commands();	
			}
		} catch(Exception exc) {
			System.out.println(exc);
		}
	}
	
	/**
	 * Inits the database.
	 * 
	 * @param args A Array of args to get connected.
	 */
	private void init(String[] args)
		throws Exception {
		m_rb = ((A_CmsInit) Class.forName(args[0]).newInstance() ).init(args[1], args[2]);
		m_cms = new CmsObject();
		m_cms.init(m_rb);
		m_cms.init(null, null, C_USER_GUEST, C_GROUP_GUEST, C_PROJECT_ONLINE);
	}	
	
	/**
	 * The commandlineinterface.
	 */
	private void commands() {
		try{
			StreamTokenizer tokenizer = new StreamTokenizer(System.in);
			tokenizer.eolIsSignificant(true);
			Vector input;
			System.out.println("Type help to get a list of commands.");
			for(;;) { // ever
				System.out.print("> ");
				input = new Vector();
				while(tokenizer.nextToken() != tokenizer.TT_EOL) {
					if(tokenizer.ttype == tokenizer.TT_NUMBER) {
						input.addElement(tokenizer.nval + "");
					} else {
						input.addElement(tokenizer.sval);
					}
				}
				// call the command
				call(input);
			}
		}catch(Exception exc){
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}
	
	/**
	 * Gives the usage-information to the user.
	 */
	private void usage() {
		// TODO: correct this usage information!
		System.out.println("Usage: java com.opencms.setup.CmsShell connectstring [init]");
	}
	
	/**
	 * Calls a command
	 * 
	 * @param command The command to be called.
	 */
	private void call(Vector command) {
		String splittet[] = new String[command.size()];
		String toCall;
		
		command.copyInto(splittet);
		toCall = splittet[0];		
		Class paramClasses[] = new Class[splittet.length - 1];
		String params[] = new String[splittet.length - 1];
		for(int z = 0; z < splittet.length - 1; z++) {
			params[z] = splittet[z+1];
			paramClasses[z] = String.class;
		}
		
		try {
			getClass().getMethod(toCall, paramClasses).invoke(this,params);
		} catch(Exception exc) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	// All methods, that may be called by the user:
	
	/**
	 * Exits the commandline-interface
	 */
	public void exit() {
		System.exit(0);
	}

	/**
	 * Prints all possible commands.
	 */
	public void help() {
		Method meth[] = getClass().getMethods();
		for(int z=0 ; z < meth.length ; z++) {
			if( ( meth[z].getDeclaringClass() == getClass() ) &&
				( meth[z].getModifiers()  == Modifier.PUBLIC ) ) {
				System.out.print(meth[z].getName() + "(");
				System.out.println(meth[z].getParameterTypes().length + ")");
			}
		}
	}
	
	/**
	 * Echos the input to output.
	 * 
	 * @param echo The echo to be written to output.
	 */
	public void echo(String echo) {
		System.out.println(echo);
	}
	
	/**
	 * Returns the current user.
	 */
	public void whoami() {
		System.out.println(m_cms.getRequestContext().currentUser());
	}

	/**
	 * Logs a user into the system.
	 * 
	 * @param username The name of the user to log in.
	 * @param password The password.
	 */
	public void login(String username, String password) {
		try {
			m_cms.loginUser(username, password);
			whoami();
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
			System.out.println("Login failed!");
		}
	}
	
	/**
	 * Returns all users of the cms.
	 */
	public void getUsers() {
		try {
			Vector users = m_cms.getUsers();
			for( int i = 0; i < users.size(); i++ ) {
				System.out.println( (A_CmsUser)users.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns all users of the cms.
	 */
	public void getGroups() {
		try {
			Vector groups = m_cms.getGroups();
			for( int i = 0; i < groups.size(); i++ ) {
				System.out.println( (A_CmsGroup)groups.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Determines, if the user is Admin.
	 */
	public void isAdmin() {
		try {
			System.out.println( m_cms.getRequestContext().isAdmin() );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Determines, if the user is Projectleader.
	 */
	public void isProjectLeader() {
		try {
			System.out.println( m_cms.getRequestContext().isProjectLeader() );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns all groups of a user.
	 * 
	 * @param username The name of the user.
	 */
	public void getGroupsOfUser(String username) {
		try {
			Vector groups = m_cms.getGroupsOfUser(username);
			for( int i = 0; i < groups.size(); i++ ) {
				System.out.println( (A_CmsGroup)groups.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}
	
	/**
	 * Adds a user to the cms.
	 * 
	 * @param name The new name for the user.
	 * @param password The new password for the user.
	 * @param group The default groupname for the user.
	 * @param description The description for the user.
	 */
	public void addUser( String name, String password, 
						 String group, String description) {
		try {
			System.out.println(m_cms.addUser( name, password, group, 
											  description, new Hashtable(), 
											  C_FLAG_ENABLED) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Adds a user to the cms.
	 * 
	 * @param name The new name for the user.
	 * @param password The new password for the user.
	 * @param group The default groupname for the user.
	 * @param description The description for the user.
	 * @param flags The flags for the user.
	 */
	public void addUser( String name, String password, 
						 String group, String description, String flags) {
		try {
			System.out.println(m_cms.addUser( name, password, group, 
											  description, new Hashtable(), 
											  Integer.parseInt(flags)) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/** 
	 * Deletes a user from the Cms.
	 * 
	 * @param name The name of the user to be deleted.
	 */
	public void deleteUser( String name ) {
		try {
			m_cms.deleteUser( name );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/** 
	 * Writes a user to the Cms.
	 * 
	 * @param name The name of the user to be written.
	 * @param flags The flags of the user to be written.
	 */
	public void writeUser( String name, String flags ) {
		try {
			// get the user, which has to be written
			A_CmsUser user = m_cms.readUser(name);
			
			if(Integer.parseInt(flags) == C_FLAG_DISABLED) {
				user.setDisabled();
			} else {
				user.setEnabled();
			}
			
			// write it back
			m_cms.writeUser(user);		

		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Adds a Group to the cms.
	 * 
	 * @param name The name of the new group.
	 * @param description The description for the new group.
	 * @int flags The flags for the new group.
	 * @param name The name of the parent group (or null).
	 */
	public void addGroup(String name, String description, String flags, String parent) {
		try {
			m_cms.addGroup( name, description, Integer.parseInt(flags), parent );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}	

	/**
	 * Adds a Group to the cms.
	 * 
	 * @param name The name of the new group.
	 * @param description The description for the new group.
	 */
	public void addGroup(String name, String description) {
		try {
			m_cms.addGroup( name, description, C_FLAG_ENABLED, null );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}	

	/**
	 * Returns a group in the Cms.
	 * 
	 * @param groupname The name of the group to be returned.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	public void readGroup(String groupname) { 
		try {
			System.out.println( m_cms.readGroup( groupname ) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}	

	/**
	 * Returns all groups of a user.
	 * 
	 * @param groupname The name of the group.
	 */
	public void getUsersOfGroup(String groupname) {
		try {
			Vector users = m_cms.getUsersOfGroup(groupname);
			for( int i = 0; i < users.size(); i++ ) {
				System.out.println( (A_CmsUser)users.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Checks if a user is member of a group.<P/>
	 *  
	 * @param nameuser The name of the user to check.
	 * @param groupname The name of the group to check.
	 */
	public void userInGroup(String username, String groupname)
	{
		try {
			System.out.println( m_cms.userInGroup( username, groupname ) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/** 
	 * Writes a group to the Cms.
	 * 
	 * @param name The name of the group to be written.
	 * @param flags The flags of the user to be written.
	 */
	public void writeGroup( String name, String flags ) {
		try {
			// get the group, which has to be written
			A_CmsGroup group = m_cms.readGroup(name);
			
			if(Integer.parseInt(flags) == C_FLAG_DISABLED) {
				group.setDisabled();
			} else {
				group.setEnabled();
			}
			
			// write it back
			m_cms.writeGroup(group);		

		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Delete a group from the Cms.<BR/>
	 * 
	 * @param delgroup The name of the group that is to be deleted.
	 */	
	public void deleteGroup(String delgroup) {
		try {
			m_cms.deleteGroup( delgroup );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Adds a user to a group.
     *
	 * @param username The name of the user that is to be added to the group.
	 * @param groupname The name of the group.
	 */	
	public void addUserToGroup(String username, String groupname) {
		try {
			m_cms.addUserToGroup( username, groupname );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Removes a user from a group.
	 * 
	 * @param username The name of the user that is to be removed from the group.
	 * @param groupname The name of the group.
	 */	
	public void removeUserFromGroup(String username, String groupname) {
		try {
			m_cms.removeUserFromGroup( username, groupname );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns all child groups of a group<P/>
	 * 
	 * @param groupname The name of the group.
	 */
	public void getChild(String groupname) {
		try {
			Vector groups = m_cms.getChild(groupname);
			for( int i = 0; i < groups.size(); i++ ) {
				System.out.println( (A_CmsGroup)groups.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}
	
	/** 
	 * Sets the password for a user.
	 * 
	 * @param username The name of the user.
	 * @param newPassword The new password.
	 */
	public void setPassword(String username, String newPassword) {
		try {
			m_cms.setPassword( username, newPassword );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

    /**
	 * Adds a new CmsMountPoint. 
	 * A new mountpoint for a mysql filesystem is added.
	 * 
	 * @param mountpoint The mount point in the Cms filesystem.
	 * @param driver The driver for the db-system. 
	 * @param connect The connectstring to access the db-system.
	 * @param name A name to describe the mountpoint.
	 */
	public void addMountPoint(String mountpoint, String driver, 
							  String connect, String name) {
		try {
			m_cms.addMountPoint( mountpoint, driver, connect, name );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Gets a CmsMountPoint. 
	 * A mountpoint will be returned.
	 * 
	 * @param mountpoint The mount point in the Cms filesystem.
	 * 
	 * @return the mountpoint - or null if it doesen't exists.
	 */
	public void readMountPoint(String mountpoint ) {
		try {
			System.out.println( m_cms.readMountPoint( mountpoint ) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Gets all CmsMountPoints. 
	 * All mountpoints will be returned.
	 * 
	 * @return the mountpoints - or null if they doesen't exists.
	 */
	public void getAllMountPoints() {
		try {
			Hashtable mountPoints = m_cms.getAllMountPoints();
			Enumeration keys = mountPoints.keys();
			
			while(keys.hasMoreElements()) {
				System.out.println(mountPoints.get(keys.nextElement()));
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

    /**
	 * Deletes a CmsMountPoint. 
	 * A mountpoint will be deleted.
	 * 
	 * @param mountpoint The mount point in the Cms filesystem.
	 */
	public void deleteMountPoint(String mountpoint ) {
		try {
			m_cms.deleteMountPoint(mountpoint);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}
	
	/**
	 * Creates a project.
	 * 
	 * @param name The name of the project to read.
	 * @param description The description for the new project.
	 * @param groupname the name of the group to be set.
	 */
	public void createProject(String name, String description, String groupname) {
		try {
			m_cms.createProject(name, description, groupname, C_FLAG_ENABLED);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Reads a project from the Cms.
	 * 
	 * @param name The name of the project to read.
	 */
	public void readProject(String name) {
		try {
			System.out.println( m_cms.readProject(name) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Reads a the online-project from the Cms.
	 */
	public void onlineProject() {
		try {
			System.out.println( m_cms.onlineProject() );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Tests if the user can access the project.
	 * 
	 * @param projectname the name of the project.
	 */
	public void accessProject(String projectname) {
		try {
			System.out.println( m_cms.accessProject(projectname) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Returns a user object.<P/>
	 * 
	 * @param username The name of the user that is to be read.
	 */
	public void readUser(String username) {
		try {
			System.out.println( m_cms.readUser(username) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Returns all projects, which the user may access.
	 * 
	 * @return a Vector of projects.
	 */
	public void getAllAccessibleProjects() {
		try {
			Vector projects = m_cms.getAllAccessibleProjects();
			for( int i = 0; i < projects.size(); i++ ) {
				System.out.println( (A_CmsProject)projects.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}
	
	/**
	 * Reads a folder from the Cms.<BR/>
	 * 
	 * @param folder The complete path to the folder that will be read.
	 */
	public void readFolder(String folder) {
		try {
			System.out.println( m_cms.readFolder(folder, "") );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Creates a new folder.
	 * 
	 * @param folder The complete path to the folder in which the new folder 
	 * will be created.
	 * @param newFolderName The name of the new folder (No pathinformation allowed).
	 */
	public void createFolder(String folder, String newFolderName) {
		try {
			System.out.println( m_cms.createFolder(folder, newFolderName) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Returns  all I_CmsResourceTypes.
	 */
	public void getAllResourceTypes() {
		try {
			Hashtable resourceTypes = m_cms.getAllResourceTypes();
			Enumeration keys = resourceTypes.keys();
			
			while(keys.hasMoreElements()) {
				System.out.println(resourceTypes.get(keys.nextElement()));
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}
	
	/**
	 * Returns a A_CmsResourceTypes.
	 * 
	 * @param resourceType the name of the resource to get.
	 */
	public void getResourceType(String resourceType) {
		try {
			System.out.println( m_cms.getResourceType(resourceType) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Adds a CmsResourceTypes.
	 * 
	 * @param resourceType the name of the resource to get.
	 * @param launcherType the launcherType-id
	 * @param launcherClass the name of the launcher-class normaly ""
	 */
	public void addResourceType(String resourceType, String launcherType, 
								String launcherClass) {		
		try {
			System.out.println( m_cms.addResourceType(resourceType, 
													  Integer.parseInt(launcherType), 
													  launcherClass) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Reads all metadefinitions for the given resource type.
	 * 
	 * @param resourcetype The name of the resource type to read the 
	 * metadefinitions for.
	 */	
	public void readAllMetadefinitions(String resourcetype) {
		try {
			Vector metadefs = m_cms.readAllMetadefinitions(resourcetype);
			for( int i = 0; i < metadefs.size(); i++ ) {
				System.out.println( (A_CmsMetadefinition)metadefs.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Creates the metadefinition for the resource type.<BR/>
	 * 
	 * @param name The name of the metadefinition to overwrite.
	 * @param resourcetype The name of the resource-type for the metadefinition.
	 * @param type The type of the metadefinition (normal|mandatory|optional)
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	public void createMetadefinition(String name, String resourcetype, String type)
		throws CmsException {
		try {
			System.out.println( m_cms.createMetadefinition(name, resourcetype, 
														   Integer.parseInt(type)) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Reads all metadefinitions for the given resource type.
	 * 
	 * @param resourcetype The name of the resource type to read the 
	 * metadefinitions for.
	 * @param type The type of the metadefinition (normal|mandatory|optional).
	 */	
	public void readAllMetadefinitions(String resourcetype, String type) {
		try {
			Vector metadefs = m_cms.readAllMetadefinitions(resourcetype, 
														   Integer.parseInt(type));
			for( int i = 0; i < metadefs.size(); i++ ) {
				System.out.println( (A_CmsMetadefinition)metadefs.elementAt(i) );
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Reads the Metadefinition for the resource type.<BR/>
	 * 
	 * @param name The name of the Metadefinition to read.
	 * @param resourcetype The name of the resource type for the Metadefinition.
	 */
	public void readMetadefinition(String name, String resourcetype) {
		try {
			System.out.println( m_cms.readMetadefinition(name, resourcetype) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Writes the Metadefinition for the resource type.<BR/>
	 * 
	 * @param name The name of the Metadefinition to overwrite.
	 * @param resourcetype The name of the resource type to read the 
	 * metadefinitions for.
	 * @param type The new type of the metadefinition (normal|mandatory|optional).
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	public void writeMetadefinition(String name, 
									String resourcetype, 
									String type) {
		try {
			A_CmsMetadefinition metadef = m_cms.readMetadefinition(name, resourcetype);
			metadef.setMetadefType(Integer.parseInt(type));			
			System.out.println( m_cms.writeMetadefinition(metadef) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Delete the Metadefinition for the resource type.<BR/>
	 * 
	 * @param name The name of the Metadefinition to overwrite.
	 * @param resourcetype The name of the resource-type for the Metadefinition.
	 */
	public void deleteMetadefinition(String name, String resourcetype) {
		try {
			m_cms.deleteMetadefinition(name, resourcetype);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns a Metainformation of a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be read.
	 * @param meta The Metadefinition-name of which the Metainformation has to be read.
	 */
	public void readMetainformation(String name, String meta) {
		try {
			System.out.println( m_cms.readMetainformation(name, meta) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}
		
	/**
	 * Writes a Metainformation for a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be set.
	 * @param meta The Metadefinition-name of which the Metainformation has to be set.
	 * @param value The value for the metainfo to be set.
	 */
	public void writeMetainformation(String name, String meta, String value) {
		try {
			m_cms.writeMetainformation(name, meta, value);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns a list of all Metainformations of a file or folder.
	 * 
	 * @param resource The name of the resource of which the Metainformation has to be 
	 * read.
	 */
	public void readAllMetainformations(String resource) {
		try {
			Hashtable metainfos = m_cms.readAllMetainformations(resource);
			Enumeration keys = metainfos.keys();
			Object key;
			
			while(keys.hasMoreElements()) {
				key = keys.nextElement();
				System.out.print(key + "=");
				System.out.println(metainfos.get(key));
			}
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}		
	}

	/**
	 * Deletes all Metainformation for a file or folder.
	 * 
	 * @param resource The name of the resource of which the Metainformations 
	 * have to be deleted.
	 */
	public void deleteAllMetainformations(String resource) {
		try {
			m_cms.deleteAllMetainformations(resource);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Deletes a Metainformation for a file or folder.
	 * 
	 * @param resourcename The resource-name of which the Metainformation has to be delteted.
	 * @param meta The Metadefinition-name of which the Metainformation has to be set.
	 */
	public void deleteMetainformation(String resourcename, String meta) {
		try {
			m_cms.deleteMetainformation(resourcename, meta);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns the anonymous user object.
	 */
	public void anonymousUser() {
		try {
			System.out.println( m_cms.anonymousUser() );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns the default group of the current user.
	 */
	public void userDefaultGroup() {
		System.out.println(m_cms.getRequestContext().userDefaultGroup());
	}

	/**
	 * Returns the current group of the current user.
	 */
	public void userCurrentGroup() {
		System.out.println(m_cms.getRequestContext().userCurrentGroup());
	}
	
	/**
	 * Sets the current group of the current user.
	 */
	public void setUserCurrentGroup(String groupname) {
		try {
			m_cms.getRequestContext().setUserCurrentGroup(groupname);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns the current project for the user.
	 */
	public void getCurrentProject() {
		System.out.println(m_cms.getRequestContext().getCurrentProject());
	}
	
	/**
	 * Sets the current project for the user.
	 * 
	 * @param projectname The name of the project to be set as current.
	 */
	public void setCurrentProject(String projectname) {
		try {
			System.out.println( m_cms.getRequestContext().setCurrentProject(projectname) );
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}

	/**
	 * Returns a version-string for this OpenCms.
	 */
	 public void version() {
		 System.out.println(m_cms.version());
	 }	 

    /**
     * Copies a resource from the online project to a new, specified project.<br>
     * Copying a resource will copy the file header or folder into the specified 
     * offline project and set its state to UNCHANGED.
     * 
	 * @param resource The name of the resource.
     */
	 public void copyResourceToProject(String resource) {
		try {
			m_cms.copyResourceToProject(resource);
		} catch( Exception exc ) {
			System.err.println(exc + "\n"  + exc.getMessage());
		}
	}
}
