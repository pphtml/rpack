package org.superbiz.service;

import org.superbiz.model.Employee;
import rx.Observable;

public interface EmployeeServiceRx {
    Observable<Employee> getEmployees();
}
