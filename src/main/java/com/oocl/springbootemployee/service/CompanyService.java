package com.oocl.springbootemployee.service;

import com.oocl.springbootemployee.model.Company;
import com.oocl.springbootemployee.model.Employee;
import com.oocl.springbootemployee.repository.CompanyRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> findAll(){
        return companyRepository.findAll();
    }

    public List<Company> findAll(int pageIndex, int pageSize) {
        return companyRepository.findAll(PageRequest.of(pageIndex - 1, pageSize)).getContent();
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id).orElse(null);
    }


    public List<Employee> getEmployeesByCompanyId(Integer id) {
        Company company = companyRepository.findById(id).orElse(null);
        return company == null ? null : company.getEmployees();
    }

    public Company create(Company company) {
        return companyRepository.save(company);
    }

    public Company update(Integer id, Company company) {
        Company companyExisted = companyRepository.findById(id).orElse(null);
        if (companyExisted == null) {
            return null;
        }
        return companyRepository.save(company);
    }

    public void delete(Integer id) {
        companyRepository.deleteById(id);
    }
}
