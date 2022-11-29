package com.mitocode.serviceImpl;

import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;
import com.mitocode.repo.IConsultaExamenRepo;
import com.mitocode.repo.IConsultaRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IConsultaService;
import jakarta.transaction.Transactional;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaServiceImpl extends CRUDImpl<Consulta,Integer>  implements IConsultaService {

	@Autowired
	private IConsultaRepo repo;
	
	@Autowired
	private IConsultaExamenRepo ceRepo;

	@Override
	protected IGenericRepo<Consulta, Integer> getRepo() {
		return repo;
	}

	@Override
	@Transactional
	public Consulta registrarTransaccional(ConsultaListaExamenDTO consultaDTO) {
		//Se inserta la consulta con su detalle consulta
		Consulta cons = new Consulta();
		consultaDTO.getConsulta().getDetalleConsulta().forEach(x -> x.setConsulta(consultaDTO.getConsulta()));
		cons = repo.save(consultaDTO.getConsulta());

		//Se inserta el examen..usamos la posicion en memoria de lo que insertamos en consulta
		consultaDTO.getListaExamen().forEach(e -> ceRepo.registrar(consultaDTO.getConsulta().getIdConsulta(), e.getIdExamen()));
		return cons;		
	}

	@Override
	public List<Consulta> buscar(FiltroConsultaDTO filtro) {
		return repo.buscar(filtro.getDui(), filtro.getNombreCompleto());
	}

	@Override
	public List<Consulta> buscarFecha(FiltroConsultaDTO filtro) {
		LocalDateTime fechaSgte = filtro.getFechaConsulta().plusDays(1);
		return repo.buscarFecha(filtro.getFechaConsulta(), fechaSgte);
	}

	@Override
	public List<ConsultaResumenDTO> listarResumen() {
		List<ConsultaResumenDTO> consultas = new ArrayList<>();
		repo.listarResumen().forEach( x -> {
			ConsultaResumenDTO cr = new ConsultaResumenDTO();
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consultas.add(cr);
		});
		return consultas;
	}

	@Override
	public byte[] generarReporte()  throws  Exception{
		byte[] data = null;
		try {
			File file = new ClassPathResource("/reports/consultas.jasper").getFile();
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), null, new JRBeanCollectionDataSource(this.listarResumen()));
			data = JasperExportManager.exportReportToPdf(print);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data;
	};
}