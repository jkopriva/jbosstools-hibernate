/*******************************************************************************
  * Copyright (c) 2010 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.hibernate.jpt.ui.internal.mapping.details.java;

import java.util.List;

import org.eclipse.jpt.jpa.ui.ResourceUiDefinition;
import org.eclipse.jpt.jpa.ui.details.DefaultMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.details.JpaUiFactory;
import org.eclipse.jpt.jpa.ui.details.MappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.BasicMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.EmbeddableUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.EmbeddedIdMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.EmbeddedMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.EntityUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.IdMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.ManyToManyMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.ManyToOneMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.MappedSuperclassUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.OneToManyMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.OneToOneMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.TransientMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.VersionMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.java.AbstractJavaResourceUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.java.DefaultBasicMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.java.DefaultEmbeddedMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.details.java.NullJavaAttributeMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.ElementCollectionMappingUiDefinition2_0;

/**
 * @author Dmitry Geraskov
 *
 */
public class Hibernate2_0JavaResourceUiDefinition extends AbstractJavaResourceUiDefinition {
	
	// FIXME: consider inheritance form Generic2_0JavaResourceUIDefinition 
	// singleton
	private static final ResourceUiDefinition INSTANCE = new Hibernate2_0JavaResourceUiDefinition();

	/**
	 * Return the singleton.
	 */
	public static ResourceUiDefinition instance() {
		return INSTANCE;
	}
	
	
	/**
	 * zero-argument constructor
	 */
	protected Hibernate2_0JavaResourceUiDefinition() {
		super();
	}
	
	@Override
	protected JpaUiFactory buildUiFactory() {
		return new Hibernate2_0JavaUiFactory();
	}
	
	@Override
	protected void addSpecifiedAttributeMappingUiDefinitionsTo(List<MappingUiDefinition> definitions) {
		definitions.add(ElementCollectionMappingUiDefinition2_0.instance());
		definitions.add(IdMappingUiDefinition.instance());
		definitions.add(EmbeddedIdMappingUiDefinition.instance());
		definitions.add(BasicMappingUiDefinition.instance());
		definitions.add(VersionMappingUiDefinition.instance());
		definitions.add(ManyToOneMappingUiDefinition.instance());
		definitions.add(OneToManyMappingUiDefinition.instance());
		definitions.add(OneToOneMappingUiDefinition.instance());
		definitions.add(ManyToManyMappingUiDefinition.instance());
		definitions.add(EmbeddedMappingUiDefinition.instance());
		definitions.add(TransientMappingUiDefinition.instance());
	}
	
	@Override
	protected void addDefaultAttributeMappingUiDefinitionsTo(List<DefaultMappingUiDefinition> definitions) {
		definitions.add(DefaultBasicMappingUiDefinition.instance());
		definitions.add(DefaultEmbeddedMappingUiDefinition.instance());
		definitions.add(NullJavaAttributeMappingUiDefinition.instance());
	}
	
	@Override
	protected void addTypeMappingUiDefinitionsTo(List<MappingUiDefinition> definitions) {
		definitions.add(EntityUiDefinition.instance());
		definitions.add(MappedSuperclassUiDefinition.instance());
		definitions.add(EmbeddableUiDefinition.instance());
	}
}
