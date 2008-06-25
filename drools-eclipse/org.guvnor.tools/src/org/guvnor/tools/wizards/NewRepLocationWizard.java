package org.guvnor.tools.wizards;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.guvnor.tools.Activator;
import org.guvnor.tools.GuvnorRepository;

public class NewRepLocationWizard extends Wizard implements INewWizard, IGuvnorWizard {
	
	private GuvnorMainConfigPage mainPage;
//	private IWorkbench workbench;
	
	private GuvWizardModel model = new GuvWizardModel();
	
	public GuvWizardModel getModel() {
		return model;
	}
	
	@Override
	public void addPages() {
		mainPage = new GuvnorMainConfigPage("config_page", "New Guvnor location", 
										Activator.getImageDescriptor(Activator.IMG_GUVLOCADD));
		super.addPage(mainPage);
		super.addPages();
	}
	
	public boolean canFinish() {
		return mainPage.isPageComplete();
	}
	
	@Override
	public boolean performFinish() {
		try {
			Activator.getLocationManager().addRepository(new GuvnorRepository(model.getRepLocation()));
			Map<String, String> info = new HashMap<String, String>();
			//TODO: Deal with case where un or password are not specified
			info.put("username", model.getUsername());
			info.put("password", model.getPassword());
			Platform.addAuthorizationInfo(new URL(model.getRepLocation()), "", "basic", info);
		} catch (Exception e) {
			Activator.getDefault().writeLog(IStatus.ERROR, e.getMessage(), e);
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
//		this.workbench = workbench;
	}
}