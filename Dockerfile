FROM maven:3-openjdk-17
RUN microdnf install jq unzip

ENV MOUNT_POINT="/opt/mount-point"
ENV SOLUTION_CODE_PATH="/opt/client/solution"
ENV COMPILE_LOG_LOCATION="/opt/client/compile.json"
COPY . $SOLUTION_CODE_PATH

WORKDIR $SOLUTION_CODE_PATH
EXPOSE 9081
CMD ["bash", "entrypoint.sh"]

RUN mvn package && rm -rf target


