package com.sriharsha.jobms.job.Impl;

import com.sriharsha.jobms.job.Job;
import com.sriharsha.jobms.job.JobRepository;
import com.sriharsha.jobms.job.JobService;
import com.sriharsha.jobms.job.clients.CompanyClient;
import com.sriharsha.jobms.job.clients.ReviewClient;
import com.sriharsha.jobms.job.dto.JobDTO;
import com.sriharsha.jobms.job.external.Company;
import com.sriharsha.jobms.job.external.Review;
import com.sriharsha.jobms.job.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

//    private List<Job> jobs = new ArrayList<>();
    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;
    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository, CompanyClient companyClient, ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient = companyClient;
        this.reviewClient = reviewClient;
    }

    @Override
    public List<JobDTO> findAll() {

        List<Job> jobs = jobRepository.findAll();
        List<JobDTO> jobDTOS = new ArrayList<>();

        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private JobDTO convertToDTO(Job job){

//            JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
//            jobWithCompanyDTO.setJob(job);
//            RestTemplate restTemplate = new RestTemplate();

            // RestTemplate
//            Company company = restTemplate.getForObject(
//                    "http://COMPANY-SERVICE:8081/companies/"+job.getCompanyId(),
//                    Company.class);

        Company company = companyClient.getCompany(job.getCompanyId());

        // RestTemplate
//           ResponseEntity<List<Review>> reviewResponse = restTemplate.exchange(
//                   "http://REVIEWS-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
//                   HttpMethod.GET,
//                   null,
//                   new ParameterizedTypeReference<List<Review>>() {
//        });
//           List<Review> reviews = reviewResponse.getBody();

        List<Review> reviews = reviewClient.getReviews(job.getCompanyId());

            JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job,company,reviews);
//            jobDTO.setCompany(company);
            return jobDTO;

    }

    @Override
    public void createJob(Job job) {

        jobRepository.save(job);
    }

    @Override
    public JobDTO getJobById(Long id) {

        Job job =  jobRepository.findById(id).orElse(null);

        return convertToDTO(job);
    }

    @Override
    public boolean deleteJobById(Long id) {

        try {
            jobRepository.deleteById(id);

            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updateJob(long id, Job updatedJob) {

        Optional<Job> jobOptional = jobRepository.findById(id);

            if (jobOptional.isPresent()){
                Job job = jobOptional.get();
                job.setTitle(updatedJob.getTitle());
                job.setDescription(updatedJob.getDescription());
                job.setMinSalary(updatedJob.getMinSalary());
                job.setMaxSalary(updatedJob.getMaxSalary());
                job.setLocation(updatedJob.getLocation());

                jobRepository.save(job);
                return  true;
            }
        return false;
    }

}
