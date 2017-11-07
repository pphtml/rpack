package org.superbiz.service;

import org.superbiz.model.Employee;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;

public interface EmployeeService {
  Promise<List<Employee>> getEmployees();
//  Operation addMeeting(Employee meeting);
//  Operation rateMeeting(String id, String rating);
}
