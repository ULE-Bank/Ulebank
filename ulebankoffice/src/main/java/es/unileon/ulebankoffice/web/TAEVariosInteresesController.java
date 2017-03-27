/**
 * 
 */
package es.unileon.ulebankoffice.web;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.unileon.ulebankoffice.domain.TAEVariosInteresesDomain;
import es.unileon.ulebankoffice.service.InteresVariable;
import es.unileon.ulebankoffice.service.TAEVariosIntereses;

/**
 * @author Razvi Razvan Raducu
 *
 */
@Controller
public class TAEVariosInteresesController {

	@RequestMapping(value = "/aprv", method = RequestMethod.GET)
	public String getAprvView(@ModelAttribute("datosTaeVariosIntereses") TAEVariosIntereses taeVariosIntereses) {
		return "aprv";
	}

	@RequestMapping(value = "/aprv", method = RequestMethod.POST)
	public String calculateAprv(ModelMap model,
			@ModelAttribute("datosTaeVariosIntereses") @Valid TAEVariosIntereses taeVariosIntereses,
			BindingResult result, HttpServletResponse response) {

		if (result.hasErrors()) {
			return "aprv";
		}

		TAEVariosInteresesDomain tae = new TAEVariosInteresesDomain(taeVariosIntereses.getCantidad(),
				taeVariosIntereses.getPeriodo(), taeVariosIntereses.getIntereses());

		List<List<String>> tabla = tae.calcular();

		// En la última posición de la tabla se encuentra el resultado, la TAE
		int tamanioTabla = tabla.size();
		// Obtengo el valor y lo elimino
		String TAE = tabla.get(tamanioTabla - 1).get(0);
		tabla.remove(tamanioTabla - 1);

		model.addAttribute("tabla", tabla);
		model.addAttribute("TAE", TAE);

		response.addCookie(new Cookie("resultados", "1"));

		return "aprv";
	}
}