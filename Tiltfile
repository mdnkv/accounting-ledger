# build
custom_build(
    ref = 'accounting-ledger',
    command = './gradlew bootBuildImage --imageName $EXPECTED_REF',
    deps = ['build.gradle', 'src']
)

# db
k8s_yaml(['k8s/postgres.yml'])
k8s_resource('accounting-ledger-db', port_forwards=['5434:5432'])

# deploy app
k8s_yaml(['k8s/deployment.yml', 'k8s/service.yml'])

# manage
k8s_resource('accounting-ledger', resource_deps=['accounting-ledger-db'])
k8s_resource('accounting-ledger', port_forwards=['8002:8000'])