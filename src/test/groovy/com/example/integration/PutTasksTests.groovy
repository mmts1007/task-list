package com.example.integration

import com.example.domain.Task
import com.example.form.TaskForm
import com.example.repository.TaskRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ['spring.datasource.url:jdbc:log4jdbc:h2:mem:tasks;DB_CLOSE_ON_EXIT=FALSE']
)
class PutTasksTests extends Specification {
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

    def '正常なリクエストの場合、ステータスコードが 200 であること'() {
        given:
        def taskForm = new TaskForm('test data', 'This task is test.', 'OPEN')

        when:
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.PUT, new HttpEntity<Object>(taskForm), Task, ['id': testData.id])

        then:
        response.statusCode == HttpStatus.OK
    }

    def '正常なリクエストの場合、ボディの内容が正しいこと'() {
        given:
        def taskForm = new TaskForm('edit test', 'This task is edited.', 'OPEN')

        when:
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.PUT, new HttpEntity<Object>(taskForm), Task, ['id': testData.id])

        then:
        def body = response.getBody()
        body.title == taskForm.title
        body.description == taskForm.description
        body.status.name() == taskForm.status
    }

    @Unroll
    def "title が #title の場合、 ステータスコードが #statusCode であること"() {
        expect:
        def taskForm = new TaskForm(title, 'This task is test.', 'OPEN')
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.PUT, new HttpEntity<Object>(taskForm), Void, ['id': testData.id])
        response.statusCode == statusCode

        where:
        title    | statusCode
        null     | HttpStatus.BAD_REQUEST
        'a' * 32 | HttpStatus.OK
        'a' * 33 | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "description が #description の場合、ステータスコードが #statusCode であること"() {
        expect:
        def taskForm = new TaskForm('title', description, 'OPEN')
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.PUT, new HttpEntity<Object>(taskForm), Void, ['id': testData.id])
        response.statusCode == statusCode

        where:
        description | statusCode
        null        | HttpStatus.OK
        'a' * 255   | HttpStatus.OK
        'a' * 256   | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "status が #status の場合、ステータスコードが #statusCode であること"() {
        expect:
        def taskForm = new TaskForm('title', 'description', status)
        def response = restTemplate.exchange("/api/tasks/{id}", HttpMethod.PUT, new HttpEntity<Object>(taskForm), Void, ['id': testData.id])
        response.statusCode == statusCode

        where:
        status   | statusCode
        null     | HttpStatus.BAD_REQUEST
        'OPEN'   | HttpStatus.OK
        'CLOSE'  | HttpStatus.OK
        'CANCEL' | HttpStatus.OK
        'DUMMY'  | HttpStatus.BAD_REQUEST
    }
}
