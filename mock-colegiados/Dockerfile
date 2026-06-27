FROM node:20-alpine
RUN npm install -g json-server@0.17.4
WORKDIR /data
COPY colegiados.json ./colegiados.json
EXPOSE 3000
CMD ["json-server", "--watch", "colegiados.json", "--host", "0.0.0.0", "--port", "3000"]