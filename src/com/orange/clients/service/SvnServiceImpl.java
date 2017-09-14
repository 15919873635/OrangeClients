package com.orange.clients.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.orange.clients.constant.ErrorCodeConst;
import com.orange.clients.constant.SVNCodeConst;
import com.orange.clients.util.BaseClientUtil;

public class SvnServiceImpl implements SvnService {
	
	static{
		SVNRepositoryFactoryImpl.setup();  
		DAVRepositoryFactory.setup();
		FSRepositoryFactory.setup();
	}
	
	private static Logger logger = Logger.getLogger(SvnServiceImpl.class);

	@Override
	public ResultMsg deleteSVNDirectory(String repositoryUrl,String userName,String password) {
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);  
	        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
	        StringBuilder commitMessage = new StringBuilder();
	        commitMessage.append("@author:").append(userName).append("\n")
	                     .append("@date:").append(BaseClientUtil.getCurrentDate()).append("\n");
	        clientManager.getCommitClient().setIgnoreExternals(false);
	        clientManager.getCommitClient().doDelete(
        		new SVNURL[]{SVNURL.parseURIEncoded(repositoryUrl)},
        		commitMessage.toString()
    		);
	        clientManager.dispose();
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.DELETE_FILE_ERROR);
		}
		return result;
	}

	@Override
	public ResultMsg checkFileExistsOnSVN(String fileUrl,String userName,String password) {
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			SVNRepository  repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(fileUrl));
			repository.setAuthenticationManager(authManager);
			SVNNodeKind nodeKind = repository.checkPath("" , -1);
			if (nodeKind == SVNNodeKind.NONE)
				result.setDatas(SVNCodeConst.NONE);
			else if ( nodeKind == SVNNodeKind.FILE)
				result.setDatas(SVNCodeConst.FILE);
			else if(nodeKind == SVNNodeKind.DIR)
				result.setDatas(SVNCodeConst.DIR);
			else
				result.setDatas(SVNCodeConst.UNKNOWN);
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.CHECK_FILE_EXISTS_ERROR);
        	result.setDatas(null);
		}
		return result;
	}
	
	@Override
	public ResultMsg addFile2SVN(File fileOrDirectory,String repositoryUrl,String userName,String password) {
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);  
	        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
	        SVNProperties svnProperties = new SVNProperties();
	        StringBuilder commitMessage = new StringBuilder();
	        commitMessage.append("@author:").append(userName).append("\n")
	                     .append("@date:").append(BaseClientUtil.getCurrentDate()).append("\n");
	        clientManager.getCommitClient().setIgnoreExternals(false);
	        clientManager.getCommitClient().doImport(
        		fileOrDirectory,
        		SVNURL.parseURIEncoded(repositoryUrl),
        		commitMessage.toString(),
        		svnProperties,
        		false,
        		true,
        		SVNDepth.INFINITY
    		);  
	        clientManager.dispose();
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.ADD_FILE_2_SVN_ERROR);
		}
		return result;
	}

	@Override
	public ResultMsg getSVNFileLastMessage(String repositoryUrl, String userName, String password) {
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			SVNRepository  repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryUrl));
			repository.setAuthenticationManager( authManager );
			Collection<?> logEntries = repository.log( new String[] {""} , null , 0 , -1 , false , true );
			Iterator<?> entries = logEntries.iterator(); 
			while (entries.hasNext()) {
			   SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			   result.setDatas(logEntry.getMessage());
			}
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.GET_FILE_MESSAGE_ERROR);
		}
		return result;
	}

	@Override
	public ResultMsg createSVNDirectory(String repositoryUrl, String userName, String password) {
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);  
	        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
	        StringBuilder commitMessage = new StringBuilder();
	        commitMessage.append("@author:").append(userName).append("\n")
	                     .append("@date:").append(BaseClientUtil.getCurrentDate()).append("\n");
	        clientManager.getCommitClient().doMkDir(
        		new SVNURL[]{SVNURL.parseURIEncoded(repositoryUrl)},
        		commitMessage.toString()
    		);
	        clientManager.dispose();
        }catch (Exception e) {
        	logger.error(e.getMessage());
		}
		return new ResultMsg();
	}

	@Override
	public ResultMsg checkOutFileFromSVN(String repositoryUrl, String basicDownloadDir, String userName, String password) {
		ResultMsg result = new ResultMsg();
		OutputStream outputStream = null;
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);  
	        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
			String nameAndSuffix = BaseClientUtil.getgetResourceNameAndSuffix(repositoryUrl);
	        StringBuilder checkoutFileDir = new StringBuilder(basicDownloadDir);
	        checkoutFileDir.append("/").append(UUID.randomUUID().toString());
	        File checkoutFile = new File(checkoutFileDir.toString());
	        boolean dirExists = checkoutFile.exists();
	        if(dirExists)
	        	dirExists = checkoutFile.delete();
	        dirExists = checkoutFile.mkdirs();
	        if(dirExists){
	        	clientManager.getUpdateClient().doExport(
	        			SVNURL.parseURIEncoded(repositoryUrl), 
	        			checkoutFile, 
	        			SVNRevision.HEAD, 
	        			SVNRevision.HEAD, 
	        			"", 
	        			true, 
	        			SVNDepth.INFINITY
    			);
				result.setDatas(new String[]{checkoutFileDir.toString(),nameAndSuffix});
	        }
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.CHECK_OUT_FILE_ERROR);
        	result.setDatas(null);
		} finally{
			try {
				if(outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return result;
	}
	
	@Override
	public ResultMsg listEntries(String repositoryUrl, String path, String userName, String password){
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);
			List<String> fileDirList = new LinkedList<String>();
			fileDirList.add(path);
			listTree(repositoryUrl,authManager,path,fileDirList);
			result.setDatas(fileDirList);
		}catch (Exception e) {
			logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.CHECK_OUT_FILE_ERROR);
        	result.setDatas(null);
		}	
		return result;
	}
	
	private void listTree(String repositoryUrl,ISVNAuthenticationManager authManager, String path, List<String> fileDirList){
		try{
			SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(repositoryUrl+path));
			repository.setAuthenticationManager(authManager);
			Collection<?> entries = repository.getDir("", -1 , null , (Collection<?>) null );
		    Iterator<?> iterator = entries.iterator();
		    while (iterator.hasNext()) {
			    SVNDirEntry entry = (SVNDirEntry) iterator.next();
			    StringBuilder fileDirbuff = new StringBuilder(path);
			    fileDirbuff.append("/").append(entry.getName());
			    fileDirList.add(fileDirbuff.toString());
			    if (entry.getKind() == SVNNodeKind.DIR) {
			    	listTree(repositoryUrl, authManager, fileDirbuff.toString(), fileDirList);
			    }
		    }
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }

	@Override
	public ResultMsg checkOutDirFromSVN(String repositoryUrl, String basicDownloadDir, String userName,
			String password) {
		ResultMsg result = new ResultMsg();
		try{
			char[] pwd = password.toCharArray();
			ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd);			
			DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);  
	        SVNClientManager clientManager = SVNClientManager.newInstance(options, authManager);
	        String nameAndSuffix = BaseClientUtil.getgetResourceNameAndSuffix(repositoryUrl);
	        StringBuilder checkoutFileDir = new StringBuilder(basicDownloadDir);
	        checkoutFileDir.append("/").append(UUID.randomUUID().toString());
	        File checkoutFile = new File(checkoutFileDir.toString());
	        boolean dirExists = checkoutFile.exists();
	        if(dirExists)
	        	dirExists = checkoutFile.delete();
	        dirExists = checkoutFile.mkdirs();
	        if(dirExists){
		        clientManager.getUpdateClient().doCheckout(
	        		SVNURL.parseURIEncoded(repositoryUrl),
	        		new File(checkoutFileDir.toString()), 
	        		SVNRevision.HEAD,
	        		SVNRevision.HEAD,
	        		SVNDepth.INFINITY,
	        		true
	    		);
	        }
	        result.setDatas(new String[]{checkoutFileDir.toString(),nameAndSuffix});
	        clientManager.dispose();
        }catch (Exception e) {
        	logger.error(e.getMessage());
        	result.setCode(ErrorCodeConst.CHECK_OUT_FILE_ERROR);
        	result.setDatas(null);
		}
		return result;
	}
}
