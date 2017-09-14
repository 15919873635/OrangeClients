package com.orange.clients.service;

import java.io.File;

public interface SvnService {
	/**
	 * 从svn删除目录结构
	 * @param repositoryUrl
	 * @param userName
	 * @param password
	 * @return
	 */
	public ResultMsg deleteSVNDirectory(String repositoryUrl,String userName,String password);
	/**
	 * 用于检查url路径是什么类型，不存在则返回NONE，是文件夹则返回DIR,是文件则返回FILE
	 * @param fileUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public ResultMsg checkFileExistsOnSVN(String fileUrl,String userName,String password);
	/**
	 * 添加文件到svn
	 * @param fileOrDirectory
	 * @param repositoryUrl
	 * @param fileVersion
	 * @param username
	 * @param password
	 * @return
	 */
	public ResultMsg addFile2SVN(File fileOrDirectory,String repositoryUrl,String userName,String password);
	/**
	 * 获取文件或目录的提交信息
	 * @param repositoryUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public ResultMsg getSVNFileLastMessage(String repositoryUrl,String userName,String password);
	
	/**
	 * 
	 * @param repositoryUrl
	 * @param username
	 * @param password
	 * @return
	 */
	public ResultMsg createSVNDirectory(String repositoryUrl,String userName,String password);
	
	/**
	 * 从svn上面checkout整个目录
	 * @param repositoryUrl
	 * @param outputStream
	 * @param userName
	 * @param password
	 * @return
	 */
	public ResultMsg checkOutFileFromSVN(String repositoryUrl, String basicDownloadDir, String userName, String password);
	
	/**
	 * 从svn上面checkout文件
	 * @param repositoryUrl
	 * @param userName
	 * @param password
	 * @return
	 */
	public ResultMsg checkOutDirFromSVN(String repositoryUrl, String basicDownloadDir, String userName, String password);
	
	/**
	 * 列出svn相关路径下的所有路径和文件
	 * @param repository
	 * @param path
	 * @return
	 */
	ResultMsg listEntries(String repository, String path, String userName, String password);
}
