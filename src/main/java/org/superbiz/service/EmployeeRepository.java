package org.superbiz.service;

import org.superbiz.model.Employee;
import ratpack.exec.Operation;
import ratpack.exec.Promise;

import java.util.List;

public interface EmployeeRepository {
  Promise<List<Employee>> getEmployees();
  //Operation addMeeting(Meeting meeting);
}
