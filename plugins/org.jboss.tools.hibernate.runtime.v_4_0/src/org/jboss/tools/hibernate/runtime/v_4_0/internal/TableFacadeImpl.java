package org.jboss.tools.hibernate.runtime.v_4_0.internal;

import org.hibernate.mapping.Table;
import org.jboss.tools.hibernate.runtime.common.AbstractTableFacade;
import org.jboss.tools.hibernate.runtime.spi.IFacadeFactory;

public class TableFacadeImpl extends AbstractTableFacade {
	
	public TableFacadeImpl(
			IFacadeFactory facadeFactory,
			Table table) {
		super(facadeFactory, table);
	}

	public Table getTarget() {
		return (Table)super.getTarget();
	}

}