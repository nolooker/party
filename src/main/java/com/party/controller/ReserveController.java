package com.party.controller;

import com.party.dto.ReserveDto;
import com.party.dto.ReserveHistDto;
import com.party.service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReserveController {
    private final ReserveService reserveService ;

    @PostMapping(value = "/reserve")
    public @ResponseBody ResponseEntity reserve(@RequestBody @Valid ReserveDto reserveDto, BindingResult error, Principal principal){
        if(error.hasErrors()){
            StringBuilder sb = new StringBuilder() ;
            List<FieldError> fieldErrors =  error.getFieldErrors();
            for(FieldError ferr : fieldErrors){
                sb.append(ferr.getDefaultMessage()) ;
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName() ;
        Long reserveId = 0L ;

        try{
            reserveId = reserveService.reserve(reserveDto, email);
            LocalDateTime startTime = reserveDto.getStartTimeAsLocalDateTime();
            LocalDateTime endTime = reserveDto.getEndTimeAsLocalDateTime();

            Integer personnel = reserveDto.getPersonnel();
            String req = reserveDto.getReq();


        }catch (Exception err){
            err.printStackTrace();
            return new ResponseEntity<String>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(reserveId, HttpStatus.OK);
    }



    @GetMapping(value = {"/reserves", "/reserves/{page}"})
    public String reserveHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 2) ;

        Page<ReserveHistDto> reserveHistDtoPage = reserveService.getReserveList(principal.getName(), pageable) ;

        model.addAttribute("reserves", reserveHistDtoPage);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "reserve/reserveHist" ;
    }







    @PostMapping(value = "/reserve/{reserveId}/cancel")
    public @ResponseBody ResponseEntity cancelReserve(@PathVariable("reserveId") Long reserveId, Principal principal){
        String email = principal.getName() ;
        if(reserveService.validateReserve(reserveId, email)){
            reserveService.cancelReserve(reserveId);
            return new ResponseEntity<Long>(reserveId, HttpStatus.OK) ;

        }else{
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN) ;
        }
    }


//
//    @GetMapping("/reservations/{reservationId}")
//    public String viewReservation(@PathVariable("reservationId") Long reservationId, Model model, Principal principal) {
//        // 주문 ID와 현재 사용자의 정보를 기반으로 예약 내역을 조회합니다.
//        boolean hasPermission = reserveService.validateReserve(reservationId, principal.getName());
//        if (!hasPermission) {
//            // 예약 내역 조회 권한이 없는 경우 예외 처리합니다.
//            throw new AccessDeniedException("주문 조회 권한이 없습니다.");
//        }
//
//        Reserve reserve = reserveService.findReservationById(reservationId);
//        model.addAttribute("reservation", reserve);
//
//        return "reserve/reserveList";
//    }



}
