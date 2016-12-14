package com.example.integration

import com.example.domain.Task
import com.example.form.TaskForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Unroll;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ['spring.datasource.url:jdbc:log4jdbc:h2:mem:tasks;DB_CLOSE_ON_EXIT=FALSE']
)
class PostTasksTests extends Specification {
    @Autowired
    TestRestTemplate restTemplate

    def '正常なリクエストの場合、ステータスコードが 201 であること'() {
        given:
        def taskForm = new TaskForm('test data', 'This task is test.', 'OPEN')

        when:
        def response = restTemplate.postForEntity('/api/tasks', taskForm, Task)

        then:
        response.statusCode == HttpStatus.CREATED
    }

    def '正常なリクエストの場合、ボディの内容が正しいこと'() {
        given:
        def taskForm = new TaskForm('test data', 'This task is test.', 'OPEN')

        when:
        def response = restTemplate.postForEntity('/api/tasks', taskForm, Task)

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
        def response = restTemplate.postForEntity('/api/tasks', taskForm, Object)
        response.statusCode == statusCode

        where:
        title    | statusCode
        null     | HttpStatus.BAD_REQUEST
        'a' * 32 | HttpStatus.CREATED
        'a' * 33 | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "description が #description の場合、ステータスコードが #statusCode であること"() {
        expect:
        def taskForm = new TaskForm('title', description, 'OPEN')
        def response = restTemplate.postForEntity('/api/tasks', taskForm, Object)
        response.statusCode == statusCode

        where:
        description | statusCode
        null        | HttpStatus.CREATED
        'a' * 255   | HttpStatus.CREATED
        'a' * 256   | HttpStatus.BAD_REQUEST
    }

    @Unroll
    def "status が #status の場合、ステータスコードが #statusCode であること"() {
        expect:
        def taskForm = new TaskForm('title', 'description', status)
        def response = restTemplate.postForEntity('/api/tasks', taskForm, Object)
        response.statusCode == statusCode

        where:
        status   | statusCode
        null     | HttpStatus.BAD_REQUEST
        'OPEN'   | HttpStatus.CREATED
        'CLOSE'  | HttpStatus.CREATED
        'CANCEL' | HttpStatus.CREATED
        'DUMMY'  | HttpStatus.BAD_REQUEST
    }
}
