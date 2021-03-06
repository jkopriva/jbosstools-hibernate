/*******************************************************************************
 * Copyright (c) 2008-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.jpt.core.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.JpaProjectManager;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.KnownConfigurationsAdapter;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Dmitry Geraskov
 *
 */
public class HibernateJptPlugin extends Plugin {

	public static final String ID = "org.jboss.tools.hibernate.jpt.core"; //$NON-NLS-1$

	private static HibernateJptPlugin inst = null;
	
	private static String JAVA_PROPERTIES_CONTENT_TYPE_NAME = "org.eclipse.jdt.core.javaProperties"; //$NON-NLS-1$
	
	public static IContentType JAVA_PROPERTIES_CONTENT_TYPE = Platform.getContentTypeManager()
			.getContentType(JAVA_PROPERTIES_CONTENT_TYPE_NAME);

	public static HibernateJptPlugin getDefault() {
		return inst;
	}

	/**
	 * Log message
	 *
	 */
	private static void log(int severity, String message, Throwable e) {
		getDefault().getLog().log(new Status(severity, ID, message, e));
	}

	/**
	 * Short exception log
	 *
	 */
	public static void logException(Throwable e) {
		log(IStatus.ERROR, e.getMessage(),  e);
	}

	/**
	 * Short exception log
	 *
	 */
	public static void logException(String message, Throwable e) {
		log(IStatus.ERROR, message,  e);
	}

	/**
	 * Short error log call
	 *
	 */
	public static void logError(String message) {
		log(IStatus.ERROR, message, null);
	}

	/**
	 * Short warning log call
	 *
	 */
	public static void logWarning(String message) {
		log(IStatus.WARNING, message, null);
	}

	/**
	 * Short information log call
	 *
	 */
	public static void logInfo(String message) {
		log(IStatus.INFO, message, null);
	}
	
	public HibernateJptPlugin(){
		if (inst == null)
			inst = this;//init default plugin
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		KnownConfigurations.getInstance().addConsoleConfigurationListener(new KnownConfigurationsAdapter(){

			private void revalidateProjects(ConsoleConfiguration ccfg){
				//INFO: should revalidate project to calculate correct naming strategy's values
				JpaProjectManager manager = (JpaProjectManager) ResourcesPlugin.getWorkspace().getAdapter(JpaProjectManager.class);
				Iterator<JpaProject> jpaProjects = manager.getJpaProjects().iterator();
				while (jpaProjects.hasNext()) {
					JpaProject jpaProject = jpaProjects.next();
					if (jpaProject instanceof HibernateJpaProject) {
						String ccName = ((HibernateJpaProject)jpaProject).getDefaultConsoleConfigurationName();
						if (ccfg.getName().equals(ccName)){
							rebuildJpaProject(jpaProject.getJavaProject().getProject());
							//jpaProject.getJavaProject().getProject().build(IncrementalProjectBuilder.FULL_BUILD, null);
						}
					}
				}
			}

			@Override
			public void configurationBuilt(ConsoleConfiguration ccfg) {
				if (ccfg.getConfiguration() == null){
					return;
				}
				revalidateProjects(ccfg);
			}

			@Override
			public void configurationRemoved(ConsoleConfiguration root,
					boolean forUpdate) {
				if(forUpdate) {
					return;
				}
				revalidateProjects(root);
			}

		});
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		inst = null;
	}

	private void rebuildJpaProject(final IProject project){
		try {
			final IWorkspaceRunnable wr = new IWorkspaceRunnable() {
				public void run(IProgressMonitor monitor)
				throws CoreException {
					try {
						((JpaProject.Reference) project.getAdapter(JpaProject.Reference.class)).rebuild();
					} catch (InterruptedException e) {
						throw new CoreException(new Status(IStatus.CANCEL, HibernateConsolePlugin.ID, null, e));
					}
					//TODO why we need this???
					project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
				}
			};

			try {
				IWorkspace ws = ResourcesPlugin.getWorkspace();
				ws.run(wr, ws.getRoot(), IWorkspace.AVOID_UPDATE, new NullProgressMonitor());
			} catch(CoreException e) {
				throw new InvocationTargetException(e);
			}
		} catch (InvocationTargetException e) {
			final Throwable te = e.getTargetException();
			throw new RuntimeException(te);
		}
	}

}
