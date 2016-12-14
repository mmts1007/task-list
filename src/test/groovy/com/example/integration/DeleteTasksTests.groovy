package com.example.integration

import com.example.domain.Task
import com.example.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ['spring.datasource.url:jdbc:log4jdbc:h2:mem:tasks;DB_CLOSE_ON_EXIT=FALSE']
)
class DeleteTasksTests extends Specification {
    @Autowired
    TaskRepository taskRepository

    @Autowired
    TestRestTemplate restTemplate

    Task testData

    def setup() {
        taskRepository.deleteAll()
        testData = new Task()
        testData.setTitle('First task')
        testData.setDescription('This task is dummy data')
        taskRepository.save(testData)
    }

    def '正常なリクエストの場合、ステータスコードが 204 であること'() {
        when:
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.DELETE, null, Void.class, ['id': testData.id])

        then:
        response.statusCode == HttpStatus.NO_CONTENT
    }

    def '存在しない id にリクエストした場合、ステータスコードが 404 であること'() {
        when:
        def response = restTemplate.exchange('/api/tasks/9999', HttpMethod.DELETE, null, Void.class)

        then:
        response.statusCode == HttpStatus.NOT_FOUND
    }
}
