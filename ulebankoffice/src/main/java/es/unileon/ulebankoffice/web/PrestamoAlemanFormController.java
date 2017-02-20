package es.unileon.ulebankoffice.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import es.unileon.ulebankoffice.domain.AlemanDomain;
import es.unileon.ulebankoffice.service.PrestamoMetodoAleman;

@Controller
public class PrestamoAlemanFormController {
	
	@RequestMapping(value = "/prestamo-metodo-aleman.htm", method = RequestMethod.POST)
    public ModelAndView processAdd(@ModelAttribute("prestamoMetodoAleman") @Valid 
    		PrestamoMetodoAleman prestamoMetodoAleman, BindingResult bindingResult, HttpServletResponse response) {
		
		ModelAndView model = new ModelAndView("prestamo-metodo-aleman");
		
		if (bindingResult.hasErrors())
        	return model;
		
		double C0 = prestamoMetodoAleman.getC0();
		double i = prestamoMetodoAleman.getI();
		int k = prestamoMetodoAleman.getK();
		int p = prestamoMetodoAleman.getP();
		
		AlemanDomain myAleman = new AlemanDomain(C0, i, k, p);
		
		List<List<String>> tabla = myAleman.calcular();
		
        model.addObject("tabla", tabla);
        
        response.addCookie(new Cookie("resultados", "1"));

        return model;	
	}
	
	@RequestMapping(value = "/prestamo-metodo-aleman.htm", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("prestamoMetodoAleman", new PrestamoMetodoAleman());
        
        return "prestamo-metodo-aleman";
    }
}