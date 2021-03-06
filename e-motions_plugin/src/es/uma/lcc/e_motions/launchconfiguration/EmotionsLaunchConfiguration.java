package es.uma.lcc.e_motions.launchconfiguration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import es.uma.lcc.e_motions.runningdata.EmotionsFileManager;

/**
 * 
 * @author Antonio Moreno-Delgado <amoreno@lcc.uma.es>
 *
 */
public class EmotionsLaunchConfiguration {
	
	protected final String FILE_NAME = ".launchemotions";
	protected final static String BEH_MODEL = "behaviorModel";
	protected final static String METAMODEL = "metamodel";
	
	protected final static String PRINT_ADV = "printAdvisories";
	protected final static String PRINT_RULES = "printRules";
	protected final static String IS_INFINITE = "isInfinite";
	protected final static String TIME_LIMIT = "timeLimit";
	
	protected final static String OUTPUT_FOLDER = "ouputFolder";
	
	protected EmotionsFileManager fm;
	protected Properties props;
	
	public EmotionsLaunchConfiguration(EmotionsFileManager fm) {
		this.fm = fm;
		props = new Properties();
	}
	
	protected boolean createProps() {
		props.setProperty(BEH_MODEL, fm.getBehaviorModel().getFullPath().toOSString());
		props.setProperty(METAMODEL, fm.getMetamodel().getFullPath().toOSString());
		
		props.setProperty(PRINT_ADV, Boolean.toString(fm.isShowAdvisories()));
		props.setProperty(PRINT_RULES, Boolean.toString(fm.isAppliedRules()));
		props.setProperty(IS_INFINITE, Boolean.toString(fm.isInfiniteLimitTime()));
		props.setProperty(TIME_LIMIT, Integer.toString(fm.getLimitTime()));
		
		props.setProperty(OUTPUT_FOLDER, fm.getFolderResult().getFullPath().toPortableString());
		return true;
	}
	
	public boolean save(IProject project) {
		boolean res;

		res = createProps();
		IFile file = project.getFile(FILE_NAME);
		try {
			FileOutputStream fos = new FileOutputStream(file.getRawLocation().makeAbsolute().toFile());
			props.store(fos, "Launch configuration for e-Motions");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public void read(IProject project) {
		IFile fileProperties = project!=null?project.getFile(FILE_NAME):null;
		if (fileProperties != null && fileProperties.exists()) {
			try {
				props.load(new FileInputStream(fileProperties.getRawLocation().makeAbsolute().toFile()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IPath path;
			IFile file;
			
			/* behavior model */
			path = new Path(props.getProperty(BEH_MODEL));
			file = root.getFile(path);
			fm.setBehaviorModel(file);
			
			/* metamodel */
			path = new Path(props.getProperty(METAMODEL));
			file = root.getFile(path);
			fm.setMetamodel(file);
			
			/* more options */
			fm.setAppliedRules(Boolean.parseBoolean(props.getProperty(PRINT_RULES)));
			fm.setShowAdvisories(Boolean.parseBoolean(props.getProperty(PRINT_ADV)));
			fm.setInfiniteLimitTime(Boolean.parseBoolean(props.getProperty(IS_INFINITE)));
			fm.setLimitTime(Integer.parseInt(props.getProperty(TIME_LIMIT)));
			
			/* output folder */
			fm.setFolderResult(ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(props.getProperty(OUTPUT_FOLDER))));
		}
	}
}
