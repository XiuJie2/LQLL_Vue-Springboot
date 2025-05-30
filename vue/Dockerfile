# 構建階段
FROM node:18 as build-stage

WORKDIR /app

# 複製依賴文件
COPY package*.json ./

# 安裝依賴
RUN npm install

# 複製源代碼
COPY . .

# 構建生產版本
RUN npm run build

# 生產階段
FROM nginx:stable-alpine as production-stage

# 從構建階段複製構建好的文件
COPY --from=build-stage /app/dist /usr/share/nginx/html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
# 暴露端口
EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]
# Nginx 已經在基礎鏡像中配置了默認啟動命令