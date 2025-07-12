// package com.maison.vinitrackpro.service;

// import java.time.LocalDateTime;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.stereotype.Service;

// import com.maison.vinitrackpro.dto.CustomerDTO;
// import com.maison.vinitrackpro.exception.ResourceNotFoundException;
// import com.maison.vinitrackpro.model.Customer;
// import com.maison.vinitrackpro.repository.CustomerRepository;
// import com.maison.vinitrackpro.repository.UserRepository;
// import com.maison.vinitrackpro.model.User;

// @Service
// public class CustomerService {

//     @Autowired
//     private CustomerRepository customerRepository;
    
//     @Autowired
//     private UserRepository userRepository;
    
//     public Page<CustomerDTO> getAllCustomers(Pageable pageable, String search) {
//         Page<Customer> customers;
//         if (search != null && !search.isEmpty()) {
//             customers = customerRepository.findByCompanyNameContaining(search, pageable);
//         } else {
//             customers = customerRepository.findAll(pageable);
//         }
//         return customers.map(this::convertToDto);
//     }
    
//     public CustomerDTO getCustomerById(Long id) {
//         Customer customer = customerRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
//         return convertToDto(customer);
//     }
    
//     @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS')")
//     public CustomerDTO createCustomer(CustomerDTO customerDTO, String username) {
//         User user = userRepository.findByUsername(username)
//             .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
//         Customer customer = new Customer();
//         // Set fields from DTO
//         customer.setCompanyName(customerDTO.getCompanyName());
//         customer.setContactPerson(customerDTO.getContactPerson());
//         customer.setEmail(customerDTO.getEmail());
//         customer.setPhone(customerDTO.getPhone());
//         customer.setBillingAddress(customerDTO.getBillingAddress());
//         customer.setShippingAddress(customerDTO.getShippingAddress());
//         customer.setPaymentTerms(customerDTO.getPaymentTerms());
//         customer.setTaxId(customerDTO.getTaxId());
        
//         customer.setCreatedBy(user);
//         customer.setCreatedAt(LocalDateTime.now());
        
//         Customer savedCustomer = customerRepository.save(customer);
//         return convertToDto(savedCustomer);
//     }
    
//     @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS')")
//     public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
//         Customer customer = customerRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        
//         // Update fields from DTO
//         customer.setCompanyName(customerDTO.getCompanyName());
//         customer.setContactPerson(customerDTO.getContactPerson());
//         customer.setEmail(customerDTO.getEmail());
//         customer.setPhone(customerDTO.getPhone());
//         customer.setBillingAddress(customerDTO.getBillingAddress());
//         customer.setShippingAddress(customerDTO.getShippingAddress());
//         customer.setPaymentTerms(customerDTO.getPaymentTerms());
//         customer.setTaxId(customerDTO.getTaxId());
        
//         Customer updatedCustomer = customerRepository.save(customer);
//         return convertToDto(updatedCustomer);
//     }
    
//     @PreAuthorize("hasRole('ADMIN')")
//     public void deleteCustomer(Long id) {
//         Customer customer = customerRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
//         customerRepository.delete(customer);
//     }
    
//     private CustomerDTO convertToDto(Customer customer) {
//         CustomerDTO dto = new CustomerDTO();
//         // Set fields from entity
//         dto.setId(customer.getId());
//         dto.setCompanyName(customer.getCompanyName());
//         dto.setContactPerson(customer.getContactPerson());
//         dto.setEmail(customer.getEmail());
//         dto.setPhone(customer.getPhone());
//         dto.setBillingAddress(customer.getBillingAddress());
//         dto.setShippingAddress(customer.getShippingAddress());
//         dto.setPaymentTerms(customer.getPaymentTerms());
//         dto.setTaxId(customer.getTaxId());
//         return dto;
//     }
// }


package com.maison.vinitrackpro.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.maison.vinitrackpro.dto.CustomerDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.Customer;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.CustomerRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Page<CustomerDTO> getAllCustomers(Pageable pageable, String search) {
        Page<Customer> customers;
        if (search != null && !search.isEmpty()) {
            customers = customerRepository.findByCompanyNameContaining(search, pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }
        return customers.map(this::convertToDto);
    }
    
    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToDto(customer);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS')")
    public CustomerDTO createCustomer(CustomerDTO customerDTO, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
            
        Customer customer = new Customer();
        // Set fields from DTO
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setBillingAddress(customerDTO.getBillingAddress());
        customer.setShippingAddress(customerDTO.getShippingAddress());
        customer.setPaymentTerms(customerDTO.getPaymentTerms());
        customer.setTaxId(customerDTO.getTaxId());
        
        customer.setCreatedBy(user);
        customer.setCreatedAt(LocalDateTime.now());
        
        Customer savedCustomer = customerRepository.save(customer);
        
        // Send welcome email to the new customer
        emailService.sendCustomerWelcomeEmail(
            savedCustomer.getEmail(),
            savedCustomer.getCompanyName(),
            savedCustomer.getContactPerson()
        );
        
        return convertToDto(savedCustomer);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS')")
    public CustomerDTO updateCustomer(Long id, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
            
        // Update fields from DTO
        customer.setCompanyName(customerDTO.getCompanyName());
        customer.setContactPerson(customerDTO.getContactPerson());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setBillingAddress(customerDTO.getBillingAddress());
        customer.setShippingAddress(customerDTO.getShippingAddress());
        customer.setPaymentTerms(customerDTO.getPaymentTerms());
        customer.setTaxId(customerDTO.getTaxId());
        
        Customer updatedCustomer = customerRepository.save(customer);
        return convertToDto(updatedCustomer);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }
    
    private CustomerDTO convertToDto(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        // Set fields from entity
        dto.setId(customer.getId());
        dto.setCompanyName(customer.getCompanyName());
        dto.setContactPerson(customer.getContactPerson());
        dto.setEmail(customer.getEmail());
        dto.setPhone(customer.getPhone());
        dto.setBillingAddress(customer.getBillingAddress());
        dto.setShippingAddress(customer.getShippingAddress());
        dto.setPaymentTerms(customer.getPaymentTerms());
        dto.setTaxId(customer.getTaxId());
        return dto;
    }
}
