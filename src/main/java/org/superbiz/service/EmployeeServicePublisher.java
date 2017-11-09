package org.superbiz.service;

import org.superbiz.model.Employee;
import ratpack.stream.TransformablePublisher;

public interface EmployeeServicePublisher {
    TransformablePublisher<Employee> getEmployees();
}
