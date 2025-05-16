package com.matunda.vinitrackpro.service;
import com.matunda.vinitrackpro.dto.SupplierDTO;
import com.matunda.vinitrackpro.exception.ResourceNotFoundException;
import com.matunda.vinitrackpro.model.Supplier;
import com.matunda.vinitrackpro.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SupplierDTO> getActiveSuppliers() {
        return supplierRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return convertToDTO(supplier);
    }

    public SupplierDTO getSupplierByEmail(String email) {
        Supplier supplier = supplierRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with email: " + email));
        return convertToDTO(supplier);
    }

    public List<SupplierDTO> getSuppliersByType(String type) {
        Supplier.SupplierType supplierType = Supplier.SupplierType.valueOf(type);
        return supplierRepository.findByType(supplierType).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<SupplierDTO> searchSuppliers(String query) {
        return supplierRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SupplierDTO toggleSupplierStatus(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        supplier.setActive(!supplier.isActive());
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDTO(updatedSupplier);
    }

    @Transactional
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        if (supplierRepository.findByEmail(supplierDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Supplier with email " + supplierDTO.getEmail() + " already exists");
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhoneNumber(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setCity(supplierDTO.getCity());
        supplier.setPostalCode(supplierDTO.getPostalCode());
        supplier.setCountry(supplierDTO.getCountry());
        supplier.setNotes(supplierDTO.getNotes());
        supplier.setType(Supplier.SupplierType.valueOf(supplierDTO.getSupplierType()));
        supplier.setActive(supplierDTO.isActive());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setTaxIdentificationNumber(supplierDTO.getTaxIdentificationNumber());

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }

    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!supplier.getEmail().equals(supplierDTO.getEmail()) && 
                supplierRepository.findByEmail(supplierDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Supplier with email " + supplierDTO.getEmail() + " already exists");
        }

        supplier.setName(supplierDTO.getName());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhoneNumber(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setCity(supplierDTO.getCity());
        supplier.setPostalCode(supplierDTO.getPostalCode());
        supplier.setCountry(supplierDTO.getCountry());
        supplier.setNotes(supplierDTO.getNotes());
        supplier.setType(Supplier.SupplierType.valueOf(supplierDTO.getSupplierType()));
        supplier.setActive(supplierDTO.isActive());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setTaxIdentificationNumber(supplierDTO.getTaxIdentificationNumber());

        Supplier updatedSupplier = supplierRepository.save(supplier);
        return convertToDTO(updatedSupplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        supplierRepository.deleteById(id);
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(supplier.getId());
        dto.setName(supplier.getName());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhoneNumber());
        dto.setAddress(supplier.getAddress());
        dto.setCity(supplier.getCity());
        dto.setPostalCode(supplier.getPostalCode());
        dto.setCountry(supplier.getCountry());
        dto.setNotes(supplier.getNotes());
        dto.setSupplierType(supplier.getType().name());
        dto.setActive(supplier.isActive());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setTaxIdentificationNumber(supplier.getTaxIdentificationNumber());
        dto.setCreatedAt(supplier.getCreatedAt());
        dto.setUpdatedAt(supplier.getUpdatedAt());
        return dto;
    }
}
