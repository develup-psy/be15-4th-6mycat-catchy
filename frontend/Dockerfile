# 1단계: Build Stage
FROM node:lts-alpine AS build-stage

# 작업 디렉토리 설정
WORKDIR /app

# 패키지 설치
COPY package*.json ./
RUN npm install

# 소스 복사 및 .env.production 포함
COPY . .

# 빌드 수행 (Vite는 .env.production 자동 인식)
RUN npm run build

# 2단계: Production Stage (Nginx로 정적 파일 서빙)
FROM nginx:stable-alpine AS production-stage

# 빌드된 정적 파일 복사
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 사용자 지정 Nginx 설정 적용
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# Nginx가 리스닝할 포트
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]