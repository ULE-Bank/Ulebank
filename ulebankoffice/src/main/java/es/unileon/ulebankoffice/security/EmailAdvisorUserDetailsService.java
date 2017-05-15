package es.unileon.ulebankoffice.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import es.unileon.ulebankoffice.domain.AdvisorUserDomain;
import es.unileon.ulebankoffice.domain.SolicitudFinancialAdvisorDomain;
import es.unileon.ulebankoffice.domain.UleBankEmployeeDomain;
import es.unileon.ulebankoffice.repository.AdvisorUserRepository;
import es.unileon.ulebankoffice.repository.UleBankEmployeeRepository;

/**
 * @author Razvan Raducu
 *
 */
@Component
public class EmailAdvisorUserDetailsService implements UserDetailsService {

	@Autowired
	private AdvisorUserRepository repo;

	private User userDetails;

	private static final Logger logger = Logger.getLogger("ulebankLogger");
	
	@Override
	public UserDetails loadUserByUsername(String userName){
		AdvisorUserDomain clienteAdvisor = repo.findByEmail(userName);

		if (clienteAdvisor == null) {
			logger.info("Un nuevo email ha accedido al consultor financiero:" + userName + " Creando registro de usuario." );
			clienteAdvisor = new AdvisorUserDomain();
			clienteAdvisor.setEmail(userName);
			clienteAdvisor.setRealizadoTest(false);
			clienteAdvisor.setResultadoTest(0.0);
			clienteAdvisor.setSolicitudes(new ArrayList<SolicitudFinancialAdvisorDomain>());
			repo.save(clienteAdvisor);
		}

		userDetails = new User(clienteAdvisor.getEmail(), "", getAuthorities());
		logger.info("Alguien ha tratado de inciar sesión en el consultor financiero con el email:" + userName + " ." );
		return userDetails;
	}

	private List<GrantedAuthority> getAuthorities() {

		List<GrantedAuthority> authList = new ArrayList<>();

		authList.add(new SimpleGrantedAuthority("ROLE_ADVISORUSER"));

		return authList;
	}

}