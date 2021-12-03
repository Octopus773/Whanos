FROM jenkins/jenkins:lts

USER root
RUN apt-get update \
    && apt-get -y install \
        apt-transport-https \
        ca-certificates \
        curl \
        software-properties-common \
    && curl -fsSL https://download.docker.com/linux/ubuntu/gpg | apt-key add - \
    && echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] http://packages.cloud.google.com/apt cloud-sdk main" | tee -a /etc/apt/sources.list.d/google-cloud-sdk.list && curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key --keyring /usr/share/keyrings/cloud.google.gpg  add - && apt-get update -y && apt-get install google-cloud-sdk -y \
    && echo "deb [arch=amd64] https://download.docker.com/linux/ubuntu xenial stable" >> /etc/apt/sources.list.d/additional-repositories.list \
    && echo "deb http://ftp-stud.hs-esslingen.de/ubuntu xenial main restricted universe multiverse" >> /etc/apt/sources.list.d/official-package-repositories.list \
    && apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 437D05B5 \
    && curl -fsSLo /usr/share/keyrings/kubernetes-archive-keyring.gpg https://packages.cloud.google.com/apt/doc/apt-key.gpg \
    && echo "deb [signed-by=/usr/share/keyrings/kubernetes-archive-keyring.gpg] https://apt.kubernetes.io/ kubernetes-xenial main" | tee /etc/apt/sources.list.d/kubernetes.list \
    && apt-get update \
    && apt-get -y install docker-ce \
    && apt-get install -y kubectl \
    && usermod -aG docker jenkins \
    && curl https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3 | bash

COPY kubernetes/AutoDeploy /helm/AutoDeploy

COPY here.json /gcloud/
#RUN gcloud auth activate-service-account whanos-jenkins-860@plucky-agency-332314.iam.gserviceaccount.com --key-file=/gcloud/here.json  --project=plucky-agency-332314
#RUN gcloud auth configure-docker europe-west1-docker.pkg.dev
#RUN gcloud config set compute/zone europe-west1-b \
#    && gcloud container clusters get-credentials cluster-1
#USER jenkins

ENV JAVA_OPTS -Djenkins.install.runSetupWizard=false
COPY jenkins/plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt
COPY images /images
COPY jenkins /jenkins
ENV CASC_JENKINS_CONFIG /jenkins/config.yml
