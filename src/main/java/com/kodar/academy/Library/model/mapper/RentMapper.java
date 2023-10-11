package com.kodar.academy.Library.model.mapper;

import com.kodar.academy.Library.model.dto.rent.RentResponseDTO;
import com.kodar.academy.Library.model.entity.Rent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RentMapper {

    private static final Logger logger = LoggerFactory.getLogger(RentMapper.class);

    public static RentResponseDTO mapToResponse(Rent source) {
        logger.info("mapToResponse called");
        RentResponseDTO target = new RentResponseDTO();
        target.setBookTitle(source.getBook().getTitle());
        target.setRentedBy(source.getUser().getUsername());
        target.setRentDate(source.getRentDate());
        target.setReturnDate(source.getReturnDate());
        target.setExpectedReturnDate(source.getExpectedReturnDate());
        return target;
    }

}
