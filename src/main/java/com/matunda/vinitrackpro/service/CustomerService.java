package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.CustomerDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Customer;
import com.matunda.vinitrackpro.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> getActiveCustomers() {
        return customerRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    public CustomerDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
        return convertToDTO(customer);
    }

    public List<CustomerDTO> getCustomersByType(String type) {
        Customer.CustomerType customerType = Customer.CustomerType.valueOf(type);
        return customerRepository.findByType(customerType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<CustomerDTO> searchCustomers(String query) {
        return customerRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDTO toggleCustomerStatus(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customer.setActive(!customer.isActive());
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Customer with email " + customerDTO.getEmail() + " already exists");
        }

        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setPostalCode(customerDTO.getPostalCode());
        customer.setCountry(customerDTO.getCountry());
        customer.setType(Customer.CustomerType.valueOf(customerDTO.getCustomerType()));
        customer.setActive(customerDTO.isActive());

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Transactional
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!customer.getEmail().equals(customerDTO.getEmail()) && 
                customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Customer with email " + customerDTO.getEmail() + " already exists");
        }

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhoneNumber(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setCity(customerDTO.getCity());
        customer.setPostalCode(customerDTO.getPostalCode());
        customer.setCountry(customerDTO.getCountry());
        customer.setType(Customer.CustomerType.valueOf(customerDTO.getCustomerType()));
        customer.setActive(customerDTO.isActive());

        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDTO(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setCity(customer.getCity());
        dto.setPostalCode(customer.getPostalCode());
        dto.setCountry(customer.getCountry());
        dto.setCustomerType(customer.getType().name());
        dto.setActive(customer.isActive());
        dto.setCreatedAt(customer.getCreatedAt());
        dto.setUpdatedAt(customer.getUpdatedAt());
        return dto;
    }
}
