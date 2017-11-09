package org.superbiz.service;

import org.jooq.DSLContext;
import org.superbiz.model.Employee;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;
import ratpack.stream.Streams;
import ratpack.stream.TransformablePublisher;

import javax.inject.Inject;
import java.util.List;

public class EmployeeServicePublisherImpl implements EmployeeServicePublisher {
    private final DSLContext dslContext;

    @Inject
    public EmployeeServicePublisherImpl(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public TransformablePublisher<Employee> getEmployees() {
        Promise<List<Employee>> promisedList = Blocking.get(() -> {
                    final List<Employee> employees = dslContext
                            .select()
                            .from(org.superbiz.model.jooq.tables.Employee.EMPLOYEE)
                            .fetchInto(Employee.class);
                    return employees;
                }
        );
        final TransformablePublisher<Employee> publisher = Streams.publish(promisedList);
        return publisher;
    }
}
