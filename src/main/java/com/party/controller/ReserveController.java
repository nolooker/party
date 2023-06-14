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

import javax.servlet.http.HttpSession;
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
    public @ResponseBody ResponseEntity reserve(@RequestBody @Valid ReserveDto reserveDto, BindingResult error, Principal principal, HttpSession session){
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

            //예약정보 세션에 저장 로그아웃시에도 캘린더에 남아있게 하는 로직
            session.setAttribute("reservationId",reserveId);

        }catch (Exception err){
            err.printStackTrace();
            return new ResponseEntity<String>(err.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(reserveId, HttpStatus.OK);
    }



    @GetMapping(value = {"/reserves", "/reserves/{page}"})
    public String reserveHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 2) ;




            Page<ReserveHistDto> reserveHistDtoPage = reserveService.getReserveList(principal.getName(), pageable);
            model.addAttribute("reserves", reserveHistDtoPage);
            model.addAttribute("page", pageable.getPageNumber());
            model.addAttribute("maxPage", 5);

        return "reserve/reserveHist" ;
    }

    @PostMapping(value = "/reserve/{reserveId}/cancel")
    public @ResponseBody ResponseEntity cancelReserve(@PathVariable("reserveId") Long reserveId, Principal principal){
        String email = principal.getName() ;
        // 세션에서 예약 ID 가져오기

        reserveService.cancelReserve(reserveId);

        return new ResponseEntity<Long>(reserveId, HttpStatus.OK) ;


        }

}
