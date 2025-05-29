// Autor: Rodrigo Escobar Fecha: 26/5/2022
package com.multi_works_group.service;

import com.multi_works_group.exceptions.ServiceException;
import com.multi_works_group.model.Quotation;
import com.multi_works_group.repository.QuotationRepository;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuotationService {
    private final QuotationRepository quotationRepository = new QuotationRepository();
    private static final Logger logger = Logger.getLogger(QuotationService.class.getName());

    // Método que guarda una cotizacion
    public void save(Quotation quotation) {
        try {
            quotationRepository.save(quotation);
            logger.info("✅ Cotización guardada: " + quotation.getId()); // Log de éxito
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "❌ Error al guardar cotización: " + e.getMessage(), e);
            throw new ServiceException("Error al guardar cotización: " + e.getMessage(), e); // Mensaje detallado
        }
    }

    // Método que busca cotizacion por su id
    public Optional<Quotation> findById(Long id) {
        try {
            return Optional.ofNullable(quotationRepository.findById(id));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar cotización", e);
            return Optional.empty();
        }
    }

    // Método para listar cotizaciones
    public List<Quotation> findAll() {
        try {
            return quotationRepository.findAll();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al listar cotizaciones", e);
            return Collections.emptyList();
        }
    }
}