package es.uma.lcc.e_motions.handlers;


import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import es.uma.lcc.e_motions.dialogs.CodGenerationDialog;
import es.uma.lcc.e_motions.jobs.CodGenerationJob;
import es.uma.lcc.e_motions.launchconfiguration.CodGenerationLaunchConfiguration;
import es.uma.lcc.e_motions.runningdata.CodGenerationFileManager;

public class CodGenerationHandler extends EmotionsHandler {
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IProject project = getSelectedProject();
		CodGenerationDialog dialog;
		fm = new CodGenerationFileManager();
		launch = new CodGenerationLaunchConfiguration((CodGenerationFileManager) fm);
		
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		if (project != null) {
			launch.read(project);
		}
		
		dialog = new CodGenerationDialog(window.getShell(), (CodGenerationFileManager) fm);
        
		int exitCode = dialog.open();
		
		if (exitCode == Window.OK) {
			getConsole();
			if (project != null) {
				launch.save(project);
			}
			CodGenerationJob job = new CodGenerationJob("CodGeneration job", (CodGenerationFileManager) fm);
			job.schedule();
		}
                     
		return null;
	}
}