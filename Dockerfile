FROM jenkins/jenkins:lts
ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
COPY jenkins/plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins .
COPY images images
ENV CASC_JENKINS_CONFIG ./config.yml
ENV ADMIN_PASSWORD password
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
