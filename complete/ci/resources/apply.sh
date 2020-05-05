fly -t hermes set-pipeline -p gs-rest-service -c ../../ci/pipelines/pipeline.yml --var "git-private-key=$(cat ~/.ssh/id_rsa)"
