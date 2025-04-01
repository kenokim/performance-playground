.PHONY: build up down test report clean wait-for-app swagger

build:
	docker-compose build

up:
	docker-compose up --abort-on-container-exit

down:
	docker-compose down

wait-for-app:
	@echo "Waiting for application to be ready..."
	@for i in $$(seq 1 30); do \
		if curl -s http://localhost:8080/actuator/health > /dev/null; then \
			echo "Application is ready!"; \
			exit 0; \
		fi; \
		echo "Waiting for application to start... ($$i/30)"; \
		sleep 2; \
	done; \
	echo "Application failed to start within 60 seconds"; \
	exit 1

test:
	docker-compose run --rm locust locust -f /mnt/locust/locustfile.py --host http://app:8080 --users 100 --spawn-rate 10 --run-time 5m --csv /mnt/reports/results

report:
	@echo "Generating performance test report..."
	@mkdir -p reports
	@echo "# Performance Test Results" > reports/README.md
	@echo "## Test Configuration" >> reports/README.md
	@echo "- Users: 100" >> reports/README.md
	@echo "- Spawn Rate: 10" >> reports/README.md
	@echo "- Run Time: 5 minutes" >> reports/README.md
	@echo "## Results" >> reports/README.md
	@if [ -f reports/results_stats.csv ]; then \
		echo "### Statistics" >> reports/README.md; \
		cat reports/results_stats.csv | grep -v "Type" | awk -F, '{print "- " $$1 ": " $$2 " requests, " $$3 " failures, " $$4 " RPS, " $$5 " avg response time"}' >> reports/README.md; \
	fi
	@echo "Report generated in reports/README.md"

clean:
	docker-compose down -v
	rm -rf reports/*
	./gradlew clean

swagger:
	@echo "Opening Swagger UI..."
	@if [ "$(shell uname)" = "Darwin" ]; then \
		open http://localhost:8080/swagger-ui.html; \
	else \
		xdg-open http://localhost:8080/swagger-ui.html; \
	fi

all: build up wait-for-app test report 