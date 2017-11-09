package org.superbiz.service;

import org.jooq.DSLContext;
import org.superbiz.model.Employee;
import rx.Observable;

import javax.inject.Inject;
import java.util.List;

public class EmployeeServiceRxImpl implements EmployeeServiceRx {
    private final DSLContext dslContext;

    @Inject
    public EmployeeServiceRxImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Observable<Employee> getEmployees() {
        final List<Employee> employees = dslContext
                .select()
                .from(org.superbiz.model.jooq.tables.Employee.EMPLOYEE)
                .fetchInto(Employee.class);

        return Observable.from(employees);
    }
}
