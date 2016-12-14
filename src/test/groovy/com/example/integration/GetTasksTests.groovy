package com.example.integration

import com.example.domain.Task
import com.example.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ['spring.datasource.url:jdbc:log4jdbc:h2:mem:tasks;DB_CLOSE_ON_EXIT=FALSE']
)
class GetTasksTests extends Specification {
    @Autowired
    TaskRepository taskRepository

    @Autowired
    TestRestTemplate restTemplate

    Task testData1
    Task testData2

    def setup() {
        taskRepository.deleteAll()
        testData1 = new Task()
        testData1.setTitle('First task')
        testData1.setDescription('This task is dummy data')
        testData2 = new Task()
        testData2.setTitle('Second task')
        testData2.setDescription('This task is dummy data')
        taskRepository.save(Arrays.asList(testData1, testData2))
    }


    def 'GET /api/tasks にリクエストした時、ステータスコードが 200 であること'() {
        when:
        def response = restTemplate.getForEntity('/api/tasks', ArrayList)

        then:
        response.statusCode == HttpStatus.OK
    }

    def 'GET /api/tasks にリクエストした時、ボディの内容がが正しいこと'() {
        when:
        def response = restTemplate.getForEntity('/api/tasks', ArrayList)

        then:
        def body = response.getBody()
        body.size() == 2
        Task responseTask1 = (Task) body.get(0)
        responseTask1 == testData1
        Task responseTask2 = (Task) body.get(1)
        responseTask2 == testData2
    }
}
