package com.opencms.file;

import java.util.*;
import javax.servlet.http.*;

import com.opencms.core.*;

/**
 * The class which implements this abstract class gains access to the OpenCms. 
 * <p>
 * The CmsObject encapsulates user identifaction and client request and is
 * the central object to transport information in the Cms Servlet.
 * <p>
 * All operations on the CmsObject are forwarded to the class which extends
 * A_CmsRessourceBroker to ensures user authentification in all operations.
 * 
 * @author Andreas Schouten
 * @version $Revision: 1.4 $ $Date: 1999/12/17 17:25:36 $ 
 */
public abstract class A_CmsObject {	

	/**
	 * Initialises the CmsObject with the resourceBroker. This only done ones!
	 * If the ressource broker was set before - it will not be overitten. This is
	 * for security reasons.
	 * 
	 * @param ressourceBroker the resourcebroker to access all resources.
	 * 
	 * @exception CmsException is thrown, when the resourceBroker was set before.
	 */
	abstract void init(A_CmsResourceBroker resourceBroker) throws CmsException;
	
	/**
	 * Initialises the CmsObject for each request.
	 * 
	 * @param requestcontext The context of this request.
	 */
	abstract void init(A_CmsRequestContext context);	
	
	/**
	 * Returns the current request-context.
	 * 
	 * @return the current request-context.
	 */
	abstract A_CmsRequestContext getRequestContext();

	/**
	 * Returns the root-folder object.
	 * 
	 * @return the root-folder object.
	 */
	abstract public CmsFolder rootFolder();
	
	/**
	 * Returns the anonymous user object.
	 * 
	 * @return the anonymous user object.
	 */
	abstract public A_CmsUser anonymousUser();
	
	/**
	 * Returns the onlineproject. This is the default project. All anonymous 
	 * (or guest) user will see the rersources of this project.
	 * 
	 * @return the onlineproject object.
	 */
	abstract public A_CmsProject onlineProject();

	/**
	 * Returns a Vector with all I_CmsResourceTypes.
	 * 
	 * Returns a Vector with all I_CmsResourceTypes.
	 */
	abstract public Vector getAllResourceTypes();
	
	/**
	 * Reads a project from the Cms.
	 * 
	 * @param name The name of the project to read.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public A_CmsProject readProject(String name)
		throws CmsException;
	
	/**
	 * Creates a project.
	 * 
	 * @param name The name of the project to read.
	 * @param description The description for the new project.
	 * @param flags The flags for the project (e.g. visibility).
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 * @exception CmsDuplicateKeyException Throws CmsDuplicateKeyException if
	 * a project with the same name for this resource-type exists already.
	 */
	abstract public A_CmsProject createProject(String name, String description, int flags)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Publishes a project.
	 * 
	 * @param name The name of the project to be published.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public A_CmsProject publishProject(String name)
		throws CmsException;
	
	/**
	 * Returns all projects, which the user may access.
	 * 
	 * @param projectname the name of the project.
	 * 
	 * @return a Vector of projects.
	 */
	abstract public Vector getAllAccessibleProjects(String projectname);
	
	/**
	 * Declines a resource. The resource can be copied to the onlineproject.
	 * 
	 * @param project The name of the project.
	 * @param resource The full path to the resource, which will be declined.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public void declineResource(String project, String resource)
		throws CmsException;

	/**
	 * Rejects a resource. The resource will be copied to the following project,
	 * at publishing time.
	 * 
	 * @param project The name of the project.
	 * @param resource The full path to the resource, which will be declined.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public void rejectResource(String project, String resource)
		throws CmsException;

	/**
	 * Creates a new file with the overgiven content and resourcetype.
	 * If there are some mandatory metadefinitions for the resourcetype, a 
	 * CmsException will be thrown, because the file cannot be created without
	 * the mandatory Metainformations.<BR/>
	 * If the resourcetype is set to folder, a CmsException will be thrown.<BR/>
	 * If there is already a file with this filename, a CmsDuplicateKey exception will
	 * be thrown.
	 * 
	 * @param folder The complete path to the folder in which the file will be created.
	 * @param filename The name of the new file (No pathinformation allowed).
	 * @param contents The contents of the new file.
	 * @param type The resourcetype of the new file.
	 * 
	 * @return file The created file.
	 * 
	 * @exception CmsException will be thrown for missing metainfos or if 
	 * resourcetype is set to folder. The CmsException is also thrown, if the 
	 * filename is not valid. The CmsException will also be thrown, if the user
	 * has not the rights for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * this name.
	 */
	abstract public CmsFile createFile(String folder, String filename, 
								byte[] contents, A_CmsResourceType type)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Creates a new file with the overgiven content and resourcetype.
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be created without
	 * the mandatory Metainformations.<BR/>
	 * If the resourcetype is set to folder, a CmsException will be thrown.<BR/>
	 * If there is already a file with this filename, a CmsDuplicateKey exception will
	 * be thrown.
	 * 
	 * @param folder The complete path to the folder in which the file will be created.
	 * @param filename The name of the new file (No pathinformation allowed).
	 * @param contents The contents of the new file.
	 * @param type The resourcetype of the new file.
	 * @param metainfos A Hashtable of metainfos, that should be set for this file.
	 * The keys for this Hashtable are the names for metadefinitions, the values are
	 * the values for the metainfos.
	 * 
	 * @return file The created file.
	 * 
	 * @exception CmsException will be thrown for missing metainfos, for worng metadefs
	 * or if resourcetype is set to folder. The CmsException is also thrown, if the 
	 * filename is not valid. The CmsException will also be thrown, if the user
	 * has not the rights for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * this name.
	 */
	abstract public CmsFile createFile(String folder, String filename, 
								byte[] contents, A_CmsResourceType type, 
								Hashtable metainfos)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Reads a file from the Cms.<BR/>
	 * 
	 * @param folder The complete path to the folder from which the file will be read.
	 * @param filename The name of the file to be read.
	 * 
	 * @return file The read file.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be read. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public CmsFile readFile(String folder, String filename)
		throws CmsException;
	
	/**
	 * Reads a file header from the Cms.<BR/>
	 * The reading excludes the filecontent.
	 * 
	 * @param folder The complete path to the folder from which the file will be read.
	 * @param filename The name of the file to be read.
	 * 
	 * @return file The read file.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be read. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public A_CmsResource readFileHeader(String folder, String filename)
		throws CmsException;
	
	/**
	 * Writes a file to the Cms.<BR/>
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be written without
	 * the mandatory Metainformations.<BR/>
	 * 
	 * @param file The file to write.
	 * 
	 * @exception CmsException will be thrown for missing metainfos, for worng metadefs
	 * or if resourcetype is set to folder. The CmsException will also be thrown, 
	 * if the user has not the rights for this resource.
	 */	
	abstract public void writeFile(CmsFile file) 
		throws CmsException;
	
	/**
	 * Writes the fileheader to the Cms.
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be written without
	 * the mandatory Metainformations.<BR/>
	 * 
	 * @param resource The resource to write the header of.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be wrote. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */	
	abstract public void writeFileHeader(A_CmsResource resource)
		throws CmsException;
	
	/**
	 * Writes a file to the Cms.<BR/>
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be written without
	 * the mandatory Metainformations.<BR/>
	 * 
	 * @param file The file to write.
	 * @param metainfos A Hashtable of metainfos, that should be set for this file.
	 * The keys for this Hashtable are the names for metadefinitions, the values are
	 * the values for the metainfos.
	 * 
	 * @exception CmsException will be thrown for missing metainfos, for worng metadefs
	 * or if resourcetype is set to folder. The CmsException will also be thrown, 
	 * if the user has not the rights for this resource.
	 */	
	abstract public void writeFile(CmsFile file, Hashtable metainfos)
		throws CmsException;
	
	/**
	 * Writes the fileheader to the Cms.
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be created without
	 * the mandatory Metainformations.<BR/>
	 * 
	 * @param resource The resource to write the header of.
	 * @param metainfos A Hashtable of metainfos, that should be set for this file.
	 * The keys for this Hashtable are the names for metadefinitions, the values are
	 * the values for the metainfos.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be wrote. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */	
	abstract public void writeFileHeader(A_CmsResource resource, Hashtable metainfos)
		throws CmsException;

	/**
	 * Renames the file to the new name.
	 * 
	 * @param oldname The complete path to the resource which will be renamed.
	 * @param newname The new name of the resource (No path information allowed).
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be renamed. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */		
	abstract public void renameFile(String oldname, String newname)
		throws CmsException;
	
	/**
	 * Deletes the file.
	 * 
	 * @param filename The complete path of the file.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be deleted. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */	
	abstract public void deleteFile(String filename)
		throws CmsException;
	
	/**
	 * Copies the file.
	 * 
	 * @param source The complete path of the sourcefile.
	 * @param destination The complete path of the destinationfile.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be copied. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * the destination filename.
	 */	
	abstract public void copyFile(String source, String destination)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Moves the file.
	 * 
	 * @param source The complete path of the sourcefile.
	 * @param destination The complete path of the destinationfile.
	 * 
	 * @exception CmsException will be thrown, if the file couldn't be moved. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * the destination filename.
	 */	
	abstract public void moveFile(String source, String destination)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Sets the resource-type of this resource.
	 * 
	 * @param resource The complete path for the resource to be changed.
	 * @param type The new type for the resource.
	 * 
	 * @exception CmsException will be thrown, if the file type couldn't be changed. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public void setResourceType(String resource, A_CmsResourceType newType)
		throws CmsException;
	
	/**
	 * Sets the resource-type of this resource.
	 * The onlineproject will be used for this resource<BR/>
	 * 
	 * @param resource The complete path for the resource to be changed.
	 * @param type The new type for the resource.
	 * @param metainfos A Hashtable of metainfos, that should be set for this file.
	 * 
	 * @exception CmsException will be thrown, if the file type couldn't be changed. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public void setResourceType(String resource, A_CmsResourceType newType, 
										 Hashtable metainfos)
		throws CmsException;

	/**
	 * Creates a new folder.
	 * If there are some mandatory metadefinitions for the folder-resourcetype, a 
	 * CmsException will be thrown, because the folder cannot be created without
	 * the mandatory Metainformations.<BR/>
	 * If there is already a folder with this filename, a CmsDuplicateKey exception 
	 * will be thrown.
	 * 
	 * @param folder The complete path to the folder in which the new folder 
	 * will be created.
	 * @param newFolderName The name of the new folder (No pathinformation allowed).
	 * 
	 * @return folder The created folder.
	 * 
	 * @exception CmsException will be thrown for missing metainfos.
	 * The CmsException is also thrown, if the foldername is not valid. 
	 * The CmsException will also be thrown, if the user has not the rights for 
	 * this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * this name.
	 */
	abstract public CmsFolder createFolder(String folder, String newFolderName)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Creates a new file with the overgiven content and resourcetype.
	 * If some mandatory metadefinitions for the resourcetype are missing, a 
	 * CmsException will be thrown, because the file cannot be created without
	 * the mandatory Metainformations.<BR/>
	 * If the resourcetype is set to folder, a CmsException will be thrown.<BR/>
	 * If there is already a file with this filename, a CmsDuplicateKey exception will
	 * be thrown.
	 * 
	 * @param folder The complete path to the folder in which the new folder will 
	 * be created.
	 * @param newFolderName The name of the new folder (No pathinformation allowed).
	 * @param metainfos A Hashtable of metainfos, that should be set for this folder.
	 * The keys for this Hashtable are the names for metadefinitions, the values are
	 * the values for the metainfos.
	 * 
	 * @return file The created file.
	 * 
	 * @exception CmsException will be thrown for missing metainfos, for worng metadefs
	 * or if the filename is not valid. The CmsException will also be thrown, if the 
	 * user has not the rights for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * this name.
	 */
	abstract public CmsFolder createFolder(String folder, String newFolderName, 
											 Hashtable metainfos)
		throws CmsException, CmsDuplicateKeyException;

	/**
	 * Reads a folder from the Cms.<BR/>
	 * 
	 * @param folder The complete path to the folder from which the folder will be 
	 * read.
	 * @param foldername The name of the folder to be read.
	 * 
	 * @return folder The read folder.
	 * 
	 * @exception CmsException will be thrown, if the folder couldn't be read. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public CmsFolder readFolder(String folder, String folderName)
		throws CmsException;
	
	/**
	 * Renames the folder to the new name.
	 * 
	 * This is a very complex operation, because all sub-resources may be
	 * renamed, too.
	 * 
	 * @param oldname The complete path to the resource which will be renamed.
	 * @param newname The new name of the resource (No path information allowed).
	 * @param force If force is set to true, all sub-resources will be renamed.
	 * If force is set to false, the folder will be renamed only if it is empty.
	 * 
	 * @exception CmsException will be thrown, if the folder couldn't be renamed. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */		
	abstract public void renameFolder(String oldname, String newname, 
									  boolean force)
		throws CmsException;
	
	/**
	 * Deletes the folder.
	 * 
	 * This is a very complex operation, because all sub-resources may be
	 * delted, too.
	 * 
	 * @param foldername The complete path of the folder.
	 * @param force If force is set to true, all sub-resources will be deleted.
	 * If force is set to false, the folder will be deleted only if it is empty.
	 * 
	 * @exception CmsException will be thrown, if the folder couldn't be deleted. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 */	
	abstract public void deleteFolder(String foldername, boolean force)
		throws CmsException;
	
	/**
	 * Copies a folder.
	 * 
	 * This is a very complex operation, because all sub-resources may be
	 * copied, too.
	 * 
	 * @param source The complete path of the sourcefolder.
	 * @param destination The complete path of the destinationfolder.
	 * @param force If force is set to true, all sub-resources will be copied.
	 * If force is set to false, the folder will be copied only if it is empty.
	 * 
	 * @exception CmsException will be thrown, if the folder couldn't be copied. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * the destination foldername.
	 */	
	abstract public void copyFolder(String source, String destination, 
									boolean force)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Moves a folder.
	 * 
	 * This is a very complex operation, because all sub-resources may be
	 * moved, too.
	 * 
	 * @param source The complete path of the sourcefile.
	 * @param destination The complete path of the destinationfile.
	 * @param force If force is set to true, all sub-resources will be moved.
	 * If force is set to false, the folder will be moved only if it is empty.
	 * 
	 * @exception CmsException will be thrown, if the folder couldn't be moved. 
	 * The CmsException will also be thrown, if the user has not the rights 
	 * for this resource.
	 * @exception CmsDuplikateKeyException if there is already a resource with 
	 * the destination filename.
	 */	
	abstract public void moveFolder(String source, String destination, 
									boolean force)
		throws CmsException, CmsDuplicateKeyException;

	/**
	 * Returns a Vector with all subfolders.<BR/>
	 * 
	 * @param foldername the complete path to the folder.
	 * 
	 * @return subfolders A Vector with all subfolders for the overgiven folder.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public Vector getSubFolders(String foldername)
		throws CmsException;
	
	/**
	 * Returns a Vector with all subfiles.<BR/>
	 * 
	 * @param foldername the complete path to the folder.
	 * 
	 * @return subfiles A Vector with all subfiles for the overgiven folder.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public Vector getFilesInFolder(String foldername)
		throws CmsException;

	/**
	 * Tests if the user may write the resource.
	 * 
	 * @param filename the complete path to the resource.
	 * 
	 * @return true, if the user may write, else returns false.
	 */
	abstract public boolean isWriteable(String filename);

	/**
	 * Tests if the resource exists.
	 * 
	 * @param filename the complete path to the resource.
	 * 
	 * @return true, if the resource exists, else returns false.
	 */
	abstract public boolean fileExists(String filename);
	
	/**
	 * Changes the flags for this resource<BR/>
	 * 
	 * The user may change the flags, if he is admin of the resource.
	 * 
	 * @param filename The complete path to the resource.
	 * @param flags The new flags for the resource.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource.
	 */
	abstract public void chmod(String filename, int flags)
		throws CmsException;
	
	/**
	 * Changes the owner for this resource<BR/>
	 * 
	 * The user may change this, if he is admin of the resource.
	 * 
	 * @param filename The complete path to the resource.
	 * @param newOwner The name of the new owner for this resource.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. It will also be thrown, if the newOwner doesn't exists.
	 */
	abstract public void chown(String filename, String newOwner)
		throws CmsException;

	/**
	 * Changes the group for this resource<BR/>
	 * 
	 * The user may change this, if he is admin of the resource.
	 * 
	 * @param filename The complete path to the resource.
	 * @param newGroup The new of the new group for this resource.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. It will also be thrown, if the newGroup doesn't exists.
	 */
	abstract public void chgrp(String filename, String newGroup)
		throws CmsException;

	/**
	 * Locks a resource<BR/>
	 * 
	 * A user can lock a resource, so he is the only one who can write this 
	 * resource.
	 * 
	 * @param resource The complete path to the resource to lock.
	 * @param force If force is true, a existing locking will be oberwritten.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. It will also be thrown, if there is a existing lock
	 * and force was set to false.
	 */
	abstract public void lockFile(String resource, boolean force)
		throws CmsException;
	
	/**
	 * Unlocks a resource<BR/>
	 * 
	 * A user can unlock a resource, so other users may lock this file.
	 * 
	 * @param resource The complete path to the resource to lock.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. It will also be thrown, if there is a existing lock
	 * and force was set to false.
	 */
	abstract public void unlockFile(String resource)
		throws CmsException;

	/**
	 * Tests, if a resource was locked<BR/>
	 * 
	 * A user can lock a resource, so he is the only one who can write this 
	 * resource. This methods checks, if a resource was locked.
	 * 
	 * @param resource The complete path to the resource.
	 * 
	 * @return true, if the resource is locked else it returns false.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. 
	 */
	abstract public boolean isLocked(String resource)
		throws CmsException;
	
	/**
	 * Returns the user, who had locked the resource.<BR/>
	 * 
	 * A user can lock a resource, so he is the only one who can write this 
	 * resource. This methods checks, if a resource was locked.
	 * 
	 * @param resource The complete path to the resource.
	 * 
	 * @return true, if the resource is locked else it returns false.
	 * 
	 * @exception CmsException will be thrown, if the user has not the rights 
	 * for this resource. 
	 */
	abstract public A_CmsUser lockedBy(String resource)
		throws CmsException;

	/**
	 * Returns a Metainformation of a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be read.
	 * @param meta The metadefinition-name of which the Metainformation has to be read.
	 * 
	 * @return metainfo The metainfo as string.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public String readMetainformation(String name, String meta)
		throws CmsException;	

	/**
	 * Writes a Metainformation for a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be set.
	 * @param meta The metadefinition-name of which the Metainformation has to be set.
	 * @param value The value for the metainfo to be set.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public void writeMetainformation(String name, String meta, String value)
		throws CmsException;

	/**
	 * Writes a couple of Metainformation for a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be set.
	 * @param metainfos A Hashtable with metadefinition- metainfo-pairs as strings.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public void writeMetainformations(String name, Hashtable metainfos)
		throws CmsException;

	/**
	 * Returns a list of all Metainformations of a file or folder.
	 * 
	 * @param name The resource-name of which the Metainformation has to be read
	 * 
	 * @return Vector of Metainformation as Strings.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public Vector readAllMetainformations(String name)
		throws CmsException;
	
	/**
	 * Deletes all Metainformation for a file or folder.
	 * 
	 * @param resourcename The resource-name of which the Metainformation has to be delteted.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public void deleteAllMetainformations(String resourcename)
		throws CmsException;

	/**
	 * Deletes a Metainformation for a file or folder.
	 * 
	 * @param resourcename The resource-name of which the Metainformation has to be delteted.
	 * @param meta The metadefinition-name of which the Metainformation has to be set.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public void deleteMetainformation(String resourcename, String meta)
		throws CmsException;

	/**
	 * Returns all users in the Cms.
	 *  
	 * @return all users in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public Vector getUsers();

	/**
	 * Returns all groups in the Cms.
	 *  
	 * @return all groups in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public Vector getGroups();	

	/**
	 * Returns a user in the Cms.
	 * 
	 * @param username The name of the user to be returned.
	 * @return a user in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public A_CmsUser readUser(String username);	

	/**
	 * Returns a user in the Cms, if the password is correct.
	 * 
	 * @param username The name of the user to be returned.
	 * @param password The password of the user to be returned.
	 * @return a user in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public A_CmsUser readUser(String username, String password);	

	/**
	 * Logs a user into the Cms, if the password is correct.
	 * 
	 * @param username The name of the user to be returned.
	 * @param password The password of the user to be returned.
	 * @return a user in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public A_CmsUser loginUser(String username, String password);
	
	/** 
	 * Adds a user to the Cms.
	 * 
	 * Only a adminstrator can add users to the cms.
	 * 
	 * @param name The new name for the user.
	 * @param password The new password for the user.
	 * @param group The default groupname for the user.
	 * @param description The description for the user.
	 * @param additionalInfos A Hashtable with additional infos for the user. These
	 * Infos may be stored into the Usertables (depending on the implementation).
	 * @param flags The flags for a user (e.g. C_FLAG_ENABLED)
	 * 
	 * @return user The added user will be returned.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesfull.
	 * @exception CmsDuplicateKeyException Throws CmsDuplicateKeyException if
	 * a user with the given username exists already.
	 */
	abstract public A_CmsUser addUser(String name, String password, String group, 
							 String description, Hashtable additionalInfos, int flags)
		throws CmsException, CmsDuplicateKeyException;
	
	/** 
	 * Deletes a user from the Cms.
	 * 
	 * Only a adminstrator can do this.
	 * 
	 * @param name The name of the user to be deleted.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesfull.
	 */
	abstract public void deleteUser(String username)
		throws CmsException;
	
	/**
	 * Updated the userinformation.<BR/>
	 * 
	 * Only the administrator can do this.
	 * 
	 * @param username The name of the user to be updated.
	 * @param additionalInfos A Hashtable with additional infos for the user. These
	 * @param flag The new user access flags.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public void updateUser(String username, Hashtable additionalInfos, int flag)
		throws CmsException;
	
	/**
	 * Gets all users in the group.
	 * 
	 * @param groupname The name of the group to get all users from.
	 * @return all users in the group.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful.
	 */
	abstract public Vector getUsersOfGroup(String groupname)
		throws CmsException;
	
	/**
	 * Gets all groups of a user.
	 * 
	 * @param username The name of the user to get all groups from.
	 * @return all groups of a user.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful.
	 */
	abstract public Vector getGroupsOfUser(String username)
		throws CmsException;
	
	/**
	 * Tests, if a user is in a group.
	 * 
	 * @param username The name of the user to test.
	 * @param groupname The name of the group to test.
	 * @return true, if the user is in the group else returns false.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful.
	 */
	abstract public boolean userInGroup(String username, String groupname)
		throws CmsException;

	/**
	 * Returns a group in the Cms.
	 * 
	 * @param groupname The name of the group to be returned.
	 * @return a group in the Cms.
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesful
	 */
	abstract public A_CmsGroup readGroup(String groupname);	
	
	/**
	 * Add a new group to the Cms.<BR/>
	 * 
	 * Only the admin can do this.
	 * 
	 * @param name The name of the new group.
	 * @param description The description for the new group.
	 * @int flags The flags for the new group.
	 *
	 * @return Group
	 * 
	 * @exception CmsException Throws CmsException if operation was not succesfull.
	 * @exception MhtDuplicateKeyException Throws MhtDuplicateKeyException if 
	 * same group already exists.
	 */	
	abstract public A_CmsGroup addGroup(String name, String description, int flags)
		throws CmsException, CmsDuplicateKeyException;
	
	/**
	 * Delete a group from the Cms.<BR/>
	 * 
	 * Only the admin can do this.
	 * 
	 * @param delgroup The name of the group that is to be deleted.
	 * @exception CmsException  Throws CmsException if operation was not succesfull.
	 */	
	abstract public void deleteGroup(String delgroup)
		throws CmsException;
	
	/**
	 * Adds a user to a group.<BR/>
     *
	 * Only the admin can do this.
	 * 
	 * @param username The name of the user that is to be added to the group.
	 * @param groupname The name of the group.
	 * @exception CmsException Throws CmsException if operation was not succesfull.
	 */	
	abstract public void addUserToGroup(String username, String groupname)
		throws CmsException;
			   
	/**
	 * Removes a user from a group.
	 * 
	 * Only the admin can do this.
	 * 
	 * @param username The name of the user that is to be removed from the group.
	 * @param groupname The name of the group.
	 * @exception CmsException Throws CmsException if operation was not succesful.
	 */	
	abstract public void removeUserFromGroup(String username, String groupname)
		throws CmsException;
	
	/**
	 * Reads the metadefinition for the resource type.<BR/>
	 * 
	 * @param name The name of the metadefinition to read.
	 * @param resourcetype The resource-type for the metadefinition.
	 * @return the metadefinition.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public A_CmsMetadefinition readMetaDefinition(String name, 
														   A_CmsResourceType type)
		throws CmsException;

	/**
	 * Reads all metadefinitions for the resource type.<BR/>
	 * 
	 * @param resourcetype The resource-type for the metadefinition.
	 * @return a Vector of metadefinitions.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public Vector getAllMetaDefinitions(A_CmsResourceType type)
		throws CmsException;

	/**
	 * Writes the metadefinition for the resource type.<BR/>
	 * 
	 * Only the admin can do this.
	 * 
	 * @param name The name of the metadefinition to overwrite.
	 * @param resourcetype The resource-type for the metadefinition.
	 * @param type The type of the metadefinition (normal|mandatory|optional)
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 * @exception CmsDuplicateKeyException Throws CmsDuplicateKeyException if
	 * a metadefinition with the same name for this resource-type exists already.
	 */
	abstract public void writeMetaDefinition(String name, A_CmsResourceType resourcetype, 
									int type)
		throws CmsDuplicateKeyException, CmsException;
	
	/**
	 * Delete the metadefinition for the resource type.<BR/>
	 * 
	 * Only the admin can do this.
	 * 
	 * @param name The name of the metadefinition to overwrite.
	 * @param resourcetype The resource-type for the metadefinition.
	 * 
	 * @exception CmsException Throws CmsException if something goes wrong.
	 */
	abstract public void deleteMetaDefinition(String name, A_CmsResourceType type)
		throws CmsException;

	/**
	 * Writes a shedule-task to the Cms.<BR/>
	 * The user of the task will be set to the current user.
	 * 
	 * @param scheduleTask the task that should be written to the Cms.
	 * 
	 * @exception CmsException if something goes wrong.
	 */
	abstract public void writeScheduleTask(A_CmsScheduleTask scheduleTask)
		throws CmsException;

	/**
	 * Deltes a shedule-task from the Cms.<BR/>
	 * 
	 * A task can only be deleted by the owner or a administrator.
	 * 
	 * @param scheduleTask the task that should be deleted.
	 * 
	 * @exception CmsException if something goes wrong.
	 */
	abstract public void deleteScheduleTask(A_CmsScheduleTask scheduleTask)
		throws CmsException;
	
	/**
	 * Reads all shedule-task from the Cms.
	 * 
	 * @return scheduleTasks A Vector with all schedule-Tasks of the Cms.
	 * 
	 * @exception CmsException if something goes wrong.
	 */
	abstract public Vector readAllScheduleTasks()
		throws CmsException;
	
	/**
	 * Gets the current valid session associated with this request, if create 
	 * is false or, if necessary, creates a new session for the request, if 
	 * create is true.
	 * 
	 * @param create decides if a new session should be created, if needed.
	 * 
	 * @return the session associated with this request or null if create 
	 * was false and no valid session is associated with this request. 
	 */
	public abstract HttpSession getSession(boolean create);
}
