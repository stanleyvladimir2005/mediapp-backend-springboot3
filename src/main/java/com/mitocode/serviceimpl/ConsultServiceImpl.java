package com.mitocode.serviceimpl;

import com.mitocode.dto.ConsultProductDTO;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.repo.IConsultExamRepo;
import com.mitocode.repo.IConsultRepo;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.IConsultService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends CRUDImpl<Consult,Integer>  implements IConsultService {

	private final IConsultRepo repo;
	private final IConsultExamRepo ceRepo;

	@Override
	protected IGenericRepo<Consult, Integer> getRepo() {
		return repo;
	}

	@Override
	@Transactional
	public Consult saveTransactional(Consult consult, List<Exam> exams) {
		repo.save(consult);
		exams.forEach(ex -> ceRepo.register(consult.getIdConsult(), ex.getIdExam()));
		return consult;
	}

	@Override
	public List<Consult> search(String dui, String fullname) {
		return repo.search(dui, fullname);
	}

	@Override
	public List<Consult> searchByDates(LocalDateTime date1, LocalDateTime date2) {
		return repo.searchByDates(date1, date2);
	}

	@Override
	public List<ConsultProductDTO> callProcedureOrFunction() {
		return repo.callProcedureOrFunction().stream()
				.map(x -> {
					val cr = new ConsultProductDTO();
					cr.setQuantity(Integer.parseInt(String.valueOf(x[0])));
					cr.setConsultdate(String.valueOf(x[1]));
					return cr;
				})
				.toList();
	}

	@Override
	public byte[] generateReport() throws Exception {
		byte[] data;
        val file = new ClassPathResource("/reports/consultas.jasper").getFile();
        val print = JasperFillManager.fillReport(file.getPath(), null, new JRBeanCollectionDataSource(this.callProcedureOrFunction()));
        data = JasperExportManager.exportReportToPdf(print);
        return data;
	}
}