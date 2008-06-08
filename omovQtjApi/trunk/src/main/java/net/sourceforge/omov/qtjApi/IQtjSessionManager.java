package net.sourceforge.omov.qtjApi;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.common.VersionMajorMinor;

public interface IQtjSessionManager {

	
	void openSession() throws BusinessException;
	
	VersionMajorMinor getQuicktimeVersion();
	
	int getJavaVersion();
	
}
