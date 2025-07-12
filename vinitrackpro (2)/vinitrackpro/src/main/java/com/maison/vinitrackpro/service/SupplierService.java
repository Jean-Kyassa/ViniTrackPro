// package com.maison.vinitrackpro.service;

// import java.time.LocalDateTime;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.access.prepost.PreAuthorize;
// import org.springframework.stereotype.Service;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;

// import com.maison.vinitrackpro.model.Supplier;
// import com.maison.vinitrackpro.model.User;
// import com.maison.vinitrackpro.dto.SupplierDTO;
// import com.maison.vinitrackpro.exception.ResourceNotFoundException;
// import com.maison.vinitrackpro.model.ProductCategory;
// import com.maison.vinitrackpro.repository.SupplierRepository;
// import com.maison.vinitrackpro.repository.UserRepository;

// @Service
// public class SupplierService {

//      @Autowired
//     private SupplierRepository supplierRepository;
    
//     @Autowired
//     private UserRepository userRepository;
    
//     public Page<SupplierDTO> getAllSuppliers(Pageable pageable, String search, ProductCategory category) {
//         Page<Supplier> suppliers;
//         if (search != null && !search.isEmpty()) {
//             suppliers = supplierRepository.findByCompanyNameContaining(search, pageable);
//         } else if (category != null) {
//             suppliers = supplierRepository.findByProductCategories(category, pageable);
//         } else {
//             suppliers = supplierRepository.findAll(pageable);
//         }
//         return suppliers.map(this::convertToDto);
//     }
    
//     public SupplierDTO getSupplierById(Long id) {
//         Supplier supplier = supplierRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
//         return convertToDto(supplier);
//     }
    
//     @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
//     public SupplierDTO createSupplier(SupplierDTO supplierDTO, String username) {
//         User user = userRepository.findByUsername(username)
//             .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
//         Supplier supplier = new Supplier();
//         // Set fields from DTO
//         supplier.setCompanyName(supplierDTO.getCompanyName());
//         supplier.setContactPerson(supplierDTO.getContactPerson());
//         supplier.setEmail(supplierDTO.getEmail());
//         supplier.setPhone(supplierDTO.getPhone());
//         supplier.setBusinessAddress(supplierDTO.getBusinessAddress());
//         supplier.setProductCategories(supplierDTO.getProductCategories());
//         supplier.setLeadTime(supplierDTO.getLeadTime());
//         supplier.setMinimumOrderQuantity(supplierDTO.getMinimumOrderQuantity());
//         supplier.setTaxId(supplierDTO.getTaxId());
//         supplier.setPaymentTerms(supplierDTO.getPaymentTerms());
//         supplier.setCurrency(supplierDTO.getCurrency());
        
//         supplier.setCreatedBy(user);
//         supplier.setCreatedAt(LocalDateTime.now());
        
//         Supplier savedSupplier = supplierRepository.save(supplier);
//         return convertToDto(savedSupplier);
//     }
    
//     @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
//     public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
//         Supplier supplier = supplierRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        
//         // Update fields from DTO
//         supplier.setCompanyName(supplierDTO.getCompanyName());
//         supplier.setContactPerson(supplierDTO.getContactPerson());
//         supplier.setEmail(supplierDTO.getEmail());
//         supplier.setPhone(supplierDTO.getPhone());
//         supplier.setBusinessAddress(supplierDTO.getBusinessAddress());
//         supplier.setProductCategories(supplierDTO.getProductCategories());
//         supplier.setLeadTime(supplierDTO.getLeadTime());
//         supplier.setMinimumOrderQuantity(supplierDTO.getMinimumOrderQuantity());
//         supplier.setTaxId(supplierDTO.getTaxId());
//         supplier.setPaymentTerms(supplierDTO.getPaymentTerms());
//         supplier.setCurrency(supplierDTO.getCurrency());
        
//         Supplier updatedSupplier = supplierRepository.save(supplier);
//         return convertToDto(updatedSupplier);
//     }
    
//     @PreAuthorize("hasRole('ADMIN')")
//     public void deleteSupplier(Long id) {
//         Supplier supplier = supplierRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
//         supplierRepository.delete(supplier);
//     }
    
//     private SupplierDTO convertToDto(Supplier supplier) {
//         SupplierDTO dto = new SupplierDTO();
//         // Set fields from entity
//         dto.setId(supplier.getId());
//         dto.setCompanyName(supplier.getCompanyName());
//         dto.setContactPerson(supplier.getContactPerson());
//         dto.setEmail(supplier.getEmail());
//         dto.setPhone(supplier.getPhone());
//         dto.setBusinessAddress(supplier.getBusinessAddress());
//         dto.setProductCategories(supplier.getProductCategories());
//         dto.setLeadTime(supplier.getLeadTime());
//         dto.setMinimumOrderQuantity(supplier.getMinimumOrderQuantity());
//         dto.setTaxId(supplier.getTaxId());
//         dto.setPaymentTerms(supplier.getPaymentTerms());
//         dto.setCurrency(supplier.getCurrency());
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

import com.maison.vinitrackpro.dto.SupplierDTO;
import com.maison.vinitrackpro.exception.ResourceNotFoundException;
import com.maison.vinitrackpro.model.ProductCategory;
import com.maison.vinitrackpro.model.Supplier;
import com.maison.vinitrackpro.model.User;
import com.maison.vinitrackpro.repository.SupplierRepository;
import com.maison.vinitrackpro.repository.UserRepository;

@Service
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    public Page<SupplierDTO> getAllSuppliers(Pageable pageable, String search, ProductCategory category) {
        Page<Supplier> suppliers;
        if (search != null && !search.isEmpty()) {
            suppliers = supplierRepository.findByCompanyNameContaining(search, pageable);
        } else if (category != null) {
            suppliers = supplierRepository.findByProductCategories(category, pageable);
        } else {
            suppliers = supplierRepository.findAll(pageable);
        }
        return suppliers.map(this::convertToDto);
    }
    
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return convertToDto(supplier);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public SupplierDTO createSupplier(SupplierDTO supplierDTO, String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
            
        Supplier supplier = new Supplier();
        // Set fields from DTO
        supplier.setCompanyName(supplierDTO.getCompanyName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setBusinessAddress(supplierDTO.getBusinessAddress());
        supplier.setProductCategories(supplierDTO.getProductCategories());
        supplier.setLeadTime(supplierDTO.getLeadTime());
        supplier.setMinimumOrderQuantity(supplierDTO.getMinimumOrderQuantity());
        supplier.setTaxId(supplierDTO.getTaxId());
        supplier.setPaymentTerms(supplierDTO.getPaymentTerms());
        supplier.setCurrency(supplierDTO.getCurrency());
        
        supplier.setCreatedBy(user);
        supplier.setCreatedAt(LocalDateTime.now());
        
        Supplier savedSupplier = supplierRepository.save(supplier);
        
        // Send welcome email to the new supplier
        emailService.sendSupplierWelcomeEmail(
            savedSupplier.getEmail(),
            savedSupplier.getCompanyName(),
            savedSupplier.getContactPerson(),
            savedSupplier.getProductCategories()
        );
        
        return convertToDto(savedSupplier);
    }
    
    @PreAuthorize("hasRole('ADMIN') or hasRole('INVENTORY')")
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
            
        // Update fields from DTO
        supplier.setCompanyName(supplierDTO.getCompanyName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setBusinessAddress(supplierDTO.getBusinessAddress());
        supplier.setProductCategories(supplierDTO.getProductCategories());
        supplier.setLeadTime(supplierDTO.getLeadTime());
        supplier.setMinimumOrderQuantity(supplierDTO.getMinimumOrderQuantity());
        supplier.setTaxId(supplierDTO.getTaxId());
        supplier.setPaymentTerms(supplierDTO.getPaymentTerms());
        supplier.setCurrency(supplierDTO.getCurrency());
        
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDto(updatedSupplier);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        supplierRepository.delete(supplier);
    }
    
    private SupplierDTO convertToDto(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        // Set fields from entity
        dto.setId(supplier.getId());
        dto.setCompanyName(supplier.getCompanyName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setBusinessAddress(supplier.getBusinessAddress());
        dto.setProductCategories(supplier.getProductCategories());
        dto.setLeadTime(supplier.getLeadTime());
        dto.setMinimumOrderQuantity(supplier.getMinimumOrderQuantity());
        dto.setTaxId(supplier.getTaxId());
        dto.setPaymentTerms(supplier.getPaymentTerms());
        dto.setCurrency(supplier.getCurrency());
        return dto;
    }
}
